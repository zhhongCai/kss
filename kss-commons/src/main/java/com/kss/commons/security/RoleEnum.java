package com.kss.commons.security;

/**
 * Created by caizh on 17-9-24.
 */
public enum RoleEnum {

    ROLE_ADMIN("管理员"),
    ROLE_NORMAL_USER("普通用户"),
    ROLE_ANONYMOUS("匿名用户");

    private String desc;

    RoleEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
