package com.kindergarten.api.common.exception;

public class CUserIncorrectPasswordException extends RuntimeException {
    public CUserIncorrectPasswordException(String msg, Throwable t) {
        super(msg, t);
    }

    public CUserIncorrectPasswordException(String msg) {
        super(msg);
    }

    public CUserIncorrectPasswordException() {
        super();
    }
}
