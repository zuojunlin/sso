package com.iquanwai.sso.filter;

import com.iquanwai.sso.context.SecurityContext;
import com.iquanwai.sso.context.SecurityContextHolder;
import com.iquanwai.sso.context.SecurityContextRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xcl
 * @version 2020/4/21
 */
public class PersistenceFilter extends BaseFilter {

    private SecurityContextRepository contextRepository;

    public PersistenceFilter(SecurityContextRepository contextRepository) {
        this.contextRepository = contextRepository;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            SecurityContext contextBeforeChainExecution =
                    contextRepository.loadContext(request, response);
            SecurityContextHolder.setContext(contextBeforeChainExecution);
            chain.doFilter(request, response);
        } finally {
            //SecurityContext contextAfterChainExecution = SecurityContextHolder.getContext();
            SecurityContextHolder.clearContext();
            //contextRepository.saveContext(contextAfterChainExecution, request, response);
        }
    }
}
