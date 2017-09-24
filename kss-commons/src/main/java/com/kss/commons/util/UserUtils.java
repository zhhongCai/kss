package com.kss.commons.util;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by caizh on 17-9-24.
 */
public class UserUtils {

    /**
     * 获取已登录用户名
     * @return
     */
    public static String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public static Object getPricipal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
