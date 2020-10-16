package com.iquanwai.sso.context;

import com.alibaba.fastjson.JSON;
import com.iquanwai.util.CommonUtils;
import com.iquanwai.util.CookieUtils;
import com.iquanwai.util.KeyBuilder;
import iquanwai.redis.client.CatRedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xcl
 * @version 2020/4/20
 */
public class RedisSecurityContextRepository implements SecurityContextRepository {

    private static final String USER_CACHE_IDAAS_PREFIX = "user:cache:idaas:context";
    private static final Integer USER_CACHE_DAYS = 60 * 60 * 24;
    private static final String TOKEN_COOKIE_NAME = "_iqt";

    @Autowired
    private CatRedisClient catRedisClient;

    @Value("${page.cookie.domain:'iquanwai.com'}")
    private String pageCookieDomain;


    @Override
    public SecurityContext loadContext(HttpServletRequest request, HttpServletResponse response) {
        String qt = CookieUtils.getCookie(request, TOKEN_COOKIE_NAME);
        String data = catRedisClient.get(KeyBuilder.withColon(USER_CACHE_IDAAS_PREFIX, qt));
        if (StringUtils.isNotEmpty(data)) {
            return JSON.parseObject(data, SecurityContext.class);
        }
        return null;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        String qt = CookieUtils.getCookie(request, TOKEN_COOKIE_NAME);
        if ((context == null || context.getAuthenticationInfo() == null)) {
            if (qt != null) {
                catRedisClient.delete(KeyBuilder.withColon(USER_CACHE_IDAAS_PREFIX, qt));
                CookieUtils.removeCookie(qt, response);
                CookieUtils.removeCookie(qt, pageCookieDomain, response);
            }
            return;
        }
        if (StringUtils.isBlank(qt)) {
            qt = CommonUtils.randomString(16);
        }
        catRedisClient.setex(KeyBuilder.withColon(USER_CACHE_IDAAS_PREFIX, qt),
                JSON.toJSONString(context), USER_CACHE_DAYS);
        String username = context.getAuthenticationInfo().getUsername();
        catRedisClient.setex(KeyBuilder.withColon(USER_CACHE_IDAAS_PREFIX, "token", username),
                qt, USER_CACHE_DAYS);
        CookieUtils.addCookie(TOKEN_COOKIE_NAME, qt, pageCookieDomain, USER_CACHE_DAYS, "/", response);
        CookieUtils.addCookie(TOKEN_COOKIE_NAME, qt, null, USER_CACHE_DAYS, "/", response);
    }

}
