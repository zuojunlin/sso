package com.iquanwai.sso.context;


import com.iquanwai.sso.authc.Authentication;

import java.io.Serializable;

/**
 * @author xcl
 * @version 2020/4/20
 */
public class SecurityContext implements Serializable {


    public SecurityContext(Authentication authenticationInfo) {
        this.authenticationInfo = authenticationInfo;
    }

    public SecurityContext() {
    }

    private Authentication authenticationInfo;

    public Authentication getAuthenticationInfo() {
        return authenticationInfo;
    }

    public void setAuthenticationInfo(Authentication authenticationInfo) {
        this.authenticationInfo = authenticationInfo;
    }
}
