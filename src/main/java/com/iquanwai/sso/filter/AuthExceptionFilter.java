package com.iquanwai.sso.filter;


import com.iquanwai.sso.exception.AuthException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xcl
 * @version 2020/4/22
 */
public class AuthExceptionFilter extends BaseFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("[[AuthExceptionFilter]]", e);
            if (e.getCause() instanceof AuthException) {
                error("无权访问", response);
                return;
            }
            throw e;
        }

    }
}
