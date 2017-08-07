package com.kss.dataprocess.application.bean.login;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class RequestLoginDto implements Serializable {

    private static final long serialVersionUID = 5550017242546092111L;

    /**
     * 登录id
     */
    @NotNull(message = "登录ID不能为空")
    private String loginId;

    /**
     * 密码
     */
    @NotNull(message = "登录密码不能为空")
    private String password;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
