package com.kss.manage.application.bean.login;

import java.io.Serializable;

public class TokenResponse implements Serializable{
    private static final long serialVersionUID = 5350017242546092969L;

    private Long id;//ID	bigint(32)
    private String code; //登陆id	varchar(32)
    private String name; //姓名
    private String phone;//手机号
    private String department; //部门名称
    private String userType;//用户类型
    private String token;//登录后产生的token

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "TokenResponse{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", department='" + department + '\'' +
                ", userType='" + userType + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
