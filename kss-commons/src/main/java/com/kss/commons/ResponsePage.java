package com.kss.commons;

import com.google.common.collect.Lists;

import java.util.List;

/**
 *
 * @param <T>
 */
public class ResponsePage<T> {
    public static int SUCCESS_CODE = 0;
    public static int ERROR_CODE = -1;

    private int code = ResponsePage.SUCCESS_CODE;
    private String msg;

    private int count;
    private List<T> data = Lists.newArrayList();

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
