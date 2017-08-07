package com.kss.commons;

import java.io.Serializable;

/**
 * 用户名称值对象，可用于标示区分人类用户与系统用户，减少在代码中重复定义的问题
 */
public abstract class UserNameInfo implements Serializable {

    private static final long serialVersionUID = -7105084013298283801L;

    private String userName;

    public UserNameInfo(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public static UserNameInfo systemUser(){
        return new SystemUserNameInfo();
    }

    public static UserNameInfo personUser(String personName){
        return new PersonUserNameInfo(personName);
    }

    abstract boolean isSystemUser();

    public static class SystemUserNameInfo extends UserNameInfo {

        private static final long serialVersionUID = 2557392728639387156L;

        private SystemUserNameInfo() {
            super("system");
        }

        @Override
        boolean isSystemUser() {
            return true;
        }
    }

    public static class PersonUserNameInfo extends UserNameInfo {

        private static final long serialVersionUID = -4233118785498964212L;

        private PersonUserNameInfo(String userName) {
            super(userName);
        }

        @Override
        boolean isSystemUser() {
            return false;
        }
    }
}
