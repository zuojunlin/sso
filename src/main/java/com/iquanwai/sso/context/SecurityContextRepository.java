package com.iquanwai.sso.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xcl
 * @version 2020/4/20
 */
public interface SecurityContextRepository {

    SecurityContext loadContext(HttpServletRequest request,
                                HttpServletResponse response);

    void saveContext(SecurityContext context, HttpServletRequest request,
                     HttpServletResponse response);
}
