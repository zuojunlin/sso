package com.iquanwai.sso.filter;



import com.iquanwai.sso.context.SecurityContext;
import com.iquanwai.sso.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author xcl
 * @version 2020/4/20
 */
public class AuthenticationFilter extends BaseFilter {
    private final static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        SecurityContext sc = SecurityContextHolder.getContext();
        if (sc == null || sc.getAuthenticationInfo() == null) {
            unLogin(request, response);
            return;
        }
        try {
            MDC.put("username",sc.getAuthenticationInfo().getName());
            chain.doFilter(request, response);
        } finally {
        }
    }


}
