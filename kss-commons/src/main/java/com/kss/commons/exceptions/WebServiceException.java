package com.kss.commons.exceptions;

/**
 * Created by chenchw on 2017/5/16.
 */
public class WebServiceException extends Exception  {
    public WebServiceException() {
    }
    public WebServiceException(Throwable cause) {
        super(cause);
    }
    public WebServiceException(String message) {
        super(message);
    }
    public WebServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}
