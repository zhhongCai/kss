package com.kss.commons.exceptions;

/**
 * BeanUtil异常类，统一处理BeanUtil转换出现的异常
 */
public class BeanException extends RuntimeException {

    public BeanException(String msg) {
        super(msg);
    }

    public BeanException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
