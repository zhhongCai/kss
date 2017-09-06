package com.kss.commons.util;

/**
 * Created by caizh on 17-9-6.
 */
public class AjaxResult<T> {
    public static final int SUCCESS_CODE = 0;
    public static final int ERROR_CODE = -1;

    private int code = SUCCESS_CODE;
    private String msg;

    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
