package com.kss.dataprocess.application.bean.user.request;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

public class RequestUserModifyDto implements Serializable {

    private static final long serialVersionUID = -5694414911675767131L;

    private Long id;	//ID	bigint(32)

    @NotEmpty(message = "登陆id不能为空")
    @Length(max = 32,message = "登录id不能超过32位")
    private String code; //登陆id	varchar(32)

    private String password; //密码

    private String confirmPassword; //确认密码

    @NotEmpty(message = "姓名不能为空")
    @Length(max = 32,message = "姓名不能超过32位")
    private String name;  //姓名

    @NotEmpty(message = "手机号不能为空")
    private String phone; //手机号

    @NotEmpty(message = "部门名称不能为空")
    @Length(max = 32,message = "部门名称不能超过32位")
    private String department; //部门名称

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
