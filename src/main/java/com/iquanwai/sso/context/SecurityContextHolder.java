package com.iquanwai.sso.context;

/**
 * @author xcl
 * @version 2020/4/20
 */
public class SecurityContextHolder {
    private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

    public static void clearContext() {
        contextHolder.remove();
    }

    public static SecurityContext getContext() {
        SecurityContext ctx = contextHolder.get();

        if (ctx == null) {
            ctx = createEmptyContext();
            contextHolder.set(ctx);
        }

        return ctx;
    }

    public static void setContext(SecurityContext context) {
        if (context == null) {
            return;
        }
        contextHolder.set(context);
    }


    public static SecurityContext createEmptyContext() {
        return new SecurityContext();
    }


}
