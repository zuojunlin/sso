package com.iquanwai.sso.config;

import com.iquanwai.sso.authz.RequirePermissionAspect;
import com.iquanwai.sso.context.RedisSecurityContextRepository;
import com.iquanwai.sso.context.SecurityContextRepository;
import com.iquanwai.sso.filter.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.List;

/**
 * @author xcl
 * @version 2020/4/27
 */
@Configuration
public class PermissionConfig {

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new RedisSecurityContextRepository();
    }

    @Bean
    public FilterChainProxy ssoFilterChainProxy() {
        SecurityContextRepository scr = securityContextRepository();
        //1.异常拦截
        Filter auth = new AuthExceptionFilter();

        // 3.线程域
        PersistenceFilter persistFilter = new PersistenceFilter(scr);

        // 6.验证拦截
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();

        List<Filter> filters = Arrays.asList(auth, persistFilter, authenticationFilter);

        FilterChainProxy filterChainProxy = new FilterChainProxy(filters);
        List<String> list = Arrays.asList("/signup", "/ok.html", "/oauth");
        filterChainProxy.setExcluedUrl(list);
        return filterChainProxy;
    }


    /**
     * 权限拦截切面
     *
     * @return
     */
    @Bean
    public RequirePermissionAspect requirePermissionAspect() {
        return new RequirePermissionAspect();
    }


}
