package com.kindergarten.api.common.exception;

public class CStudentNotFoundException extends RuntimeException {
    public CStudentNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public CStudentNotFoundException(String msg) {
        super(msg);
    }

    public CStudentNotFoundException() {
        super();
    }
}
