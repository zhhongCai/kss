package com.kss.commons.exceptions;

public class ServiceException extends RuntimeException {

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(Exception e) {
        super(e);
    }
}
