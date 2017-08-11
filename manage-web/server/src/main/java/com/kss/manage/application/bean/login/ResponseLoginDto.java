package com.kss.manage.application.bean.login;

import java.io.Serializable;


public class ResponseLoginDto implements Serializable {

    private static final long serialVersionUID = 7309508091994332648L;
    
    private Long id;//ID	bigint(32)
    private String code; //登陆id	varchar(32)
    private String name; //姓名
    private String phone;//手机号
    private String department; //部门名称
    private String userType;//用户类型
    private String token;//登录后产生的token

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
}
