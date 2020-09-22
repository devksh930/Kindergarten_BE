package com.kindergarten.api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException e) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("ERROR_MSG", e.getMessage());
        msg.put("status", e.getHttpStatus());
        return new ResponseEntity(msg, e.getHttpStatus());

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("ERROR_MSG", e.getMessage());
        return new ResponseEntity(msg, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
