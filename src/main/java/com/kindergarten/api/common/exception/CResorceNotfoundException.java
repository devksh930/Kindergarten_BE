package com.kindergarten.api.common.exception;

public class CResorceNotfoundException extends RuntimeException {
    public CResorceNotfoundException(String msg, Throwable t) {
        super(msg, t);
    }

    public CResorceNotfoundException(String msg) {
        super(msg);
    }

    public CResorceNotfoundException() {
        super();
    }
}
