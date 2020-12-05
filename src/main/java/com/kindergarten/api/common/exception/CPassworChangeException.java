package com.kindergarten.api.common.exception;

public class CPassworChangeException extends RuntimeException {
    public CPassworChangeException(String msg, Throwable t) {
        super(msg, t);
    }

    public CPassworChangeException(String msg) {
        super(msg);
    }

    public CPassworChangeException() {
        super();
    }
}