package com.kss.commons.exceptions;

public class AuthorizationException extends Exception  {
    public AuthorizationException() {
    }
    public AuthorizationException(Throwable cause) {
        super(cause);
    }
    public AuthorizationException(String message) {
        super(message);
    }
    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}
