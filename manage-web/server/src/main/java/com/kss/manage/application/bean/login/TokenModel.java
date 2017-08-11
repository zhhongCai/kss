package com.kss.manage.application.bean.login;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class TokenModel implements Serializable{
    private static final long serialVersionUID = 5350017242546092109L;
    //用户id
    @NotNull
    private Long userId;

    @NotNull
    private String token;

    public TokenModel(){

    }

    public TokenModel(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "TokenModel{" +
                "userId=" + userId +
                ", token='" + token + '\'' +
                '}';
    }
}
