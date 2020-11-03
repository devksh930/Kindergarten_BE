package com.kindergarten.api.common.exception;

public class CReviewExistException extends RuntimeException {
    public CReviewExistException(String msg, Throwable t) {
        super(msg, t);
    }

    public CReviewExistException(String msg) {
        super(msg);
    }

    public CReviewExistException() {
        super();
    }
}
