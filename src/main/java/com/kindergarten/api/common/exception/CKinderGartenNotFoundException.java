package com.kindergarten.api.common.exception;

public class CKinderGartenNotFoundException extends RuntimeException {
    public CKinderGartenNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public CKinderGartenNotFoundException(String msg) {
        super(msg);
    }

    public CKinderGartenNotFoundException() {
        super();
    }
}
