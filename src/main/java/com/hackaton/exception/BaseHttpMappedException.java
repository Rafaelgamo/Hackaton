package com.hackaton.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseHttpMappedException extends RuntimeException {
    private final HttpStatus httpStatus;

    public abstract HttpStatus getHttpStatus();

    public BaseHttpMappedException(String message) {
        super(message);
        this.httpStatus = getHttpStatus();
    }

    public BaseHttpMappedException(String message, Exception exception) {
        super(message, exception);
        this.httpStatus = getHttpStatus();
    }

}

