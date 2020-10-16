package com.iquanwai.sso.authz;

import com.iquanwai.sso.authc.Authentication;
import com.iquanwai.sso.context.SecurityContextHolder;
import com.iquanwai.sso.exception.AuthException;
import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author xcl
 * @version 2020/4/22
 */
@Aspect
public class RequirePermissionAspect {

    @Pointcut("@annotation(com.iquanwai.sso.authz.RequirePermission)")
    public void requirePermission() {
    }

    @Before("requirePermission()")
    public void requirePermissionExec(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequirePermission rp = method.getAnnotation(RequirePermission.class);
        if (rp != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthenticationInfo();
            List<String> ps = auth.getPermissions();
            String[] values = rp.value();
            if (!CollectionUtils.containsAny(ps, Arrays.asList(values))) {
               throw new AuthException("[[permission deny]]" + Arrays.asList(values));
            }
        }
    }
}
