package com.kss.commons.exceptions;

/**
 * Created by caijiacheng on 30/05/2017.
 */
public class ExceptionBean {

    public ExceptionBean() {
    }

    public ExceptionBean(int code, String error) {
        this.code = code;
        this.error = error;
    }

    int code;
    String error;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

