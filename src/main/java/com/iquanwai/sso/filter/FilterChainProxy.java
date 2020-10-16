package com.iquanwai.sso.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author xcl
 * @version 2020/4/20
 */
public class FilterChainProxy extends GenericFilterBean {

    private final static String FILTER_APPLIED = FilterChainProxy.class.getName().concat(
            ".APPLIED");
    private static final Log logger = LogFactory.getLog(FilterChainProxy.class);

    private List<Filter> filters;

    private List<String> excludeUrls;


    public FilterChainProxy(List<Filter> filters) {
        this.filters = filters;
    }

    /**
     * 内部服务调用
     *
     * @param uri
     * @return
     */
    private boolean isInternal(String uri) {
        return uri.startsWith("/internal");
    }

    private boolean isExcluedRequest(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();
        if (isInternal(uri)) {
            return true;
        }
        for (int i = 0; i < this.excludeUrls.size(); i++) {
            String exclude = excludeUrls.get(i);
            if (uri.startsWith(exclude)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean hasAlreadyFilteredAttribute = request.getAttribute(FILTER_APPLIED) != null;
        if (hasAlreadyFilteredAttribute) {
            try {
                request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
                chain.doFilter(request, response);
            } finally {
                request.removeAttribute(FILTER_APPLIED);
            }
        } else {
            doFilterInternal(request, response, chain);
        }
    }

    private void doFilterInternal(ServletRequest request, ServletResponse response,
                                  FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        List<Filter> filters = getFilters(httpRequest);
        if (filters == null || filters.size() == 0 || isExcluedRequest(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }
        VirtualFilterChain vfc = new VirtualFilterChain(httpRequest, chain, filters);
        vfc.doFilter(request, httpResponse);
    }

    private List<Filter> getFilters(HttpServletRequest request) {
        if(isExcluedRequest(request)){
            return null;
        }
        return filters;
    }

    private static class VirtualFilterChain implements FilterChain {
        private final FilterChain originalChain;
        private final List<Filter> additionalFilters;
        private final HttpServletRequest firewalledRequest;
        private final int size;
        private int currentPosition = 0;

        private VirtualFilterChain(HttpServletRequest firewalledRequest,
                                   FilterChain chain, List<Filter> additionalFilters) {
            this.originalChain = chain;
            this.additionalFilters = additionalFilters;
            this.size = additionalFilters.size();
            this.firewalledRequest = firewalledRequest;
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response)
                throws IOException, ServletException {
            if (currentPosition == size) {
                originalChain.doFilter(request, response);
            } else {
                currentPosition++;
                Filter nextFilter = additionalFilters.get(currentPosition - 1);
                nextFilter.doFilter(request, response, this);
            }
        }
    }

    public void setExcluedUrl(List<String> excluedUrl) {
        this.excludeUrls = excluedUrl;
    }
}
