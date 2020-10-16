package com.iquanwai.sso.filter;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xcl
 * @version 2020/4/20
 */
public abstract class BaseFilter implements Filter {
    public static final String ALREADY_FILTERED_SUFFIX = ".FILTERED";
    protected final static Logger logger = LoggerFactory.getLogger(BaseFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new ServletException("OncePerRequestFilter just supports HTTP requests");
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();
        boolean hasAlreadyFilteredAttribute = request.getAttribute(alreadyFilteredAttributeName) != null;
        if (hasAlreadyFilteredAttribute) {
            chain.doFilter(request, response);
        } else {
            request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);
            try {
                doFilterInternal(httpRequest, httpResponse, chain);
            } finally {
                request.removeAttribute(alreadyFilteredAttributeName);
            }
        }
    }

    protected String getAlreadyFilteredAttributeName() {
        String name = getClass().getName();
        return name + ALREADY_FILTERED_SUFFIX;
    }

    public abstract void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException;


    protected void unLogin(ServletRequest servletRequest, ServletResponse response) {
        try {
            // ajax请求，返回700
            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
            Map<String, Object> json = new HashMap<>();
            json.put("code", 700);
            json.put("message", "未登录");
            writer.write(JSON.toJSONString(json));
            writer.close();
            response.flushBuffer();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    protected void error(String message, ServletResponse response) {
        try {
            // ajax请求，返回700
            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
            Map<String, Object> json = new HashMap<>();
            json.put("code", 400);
            json.put("message", message);
            writer.write(JSON.toJSONString(json));
            writer.close();
            response.flushBuffer();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
