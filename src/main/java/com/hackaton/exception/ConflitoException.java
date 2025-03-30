package com.hackaton.exception;

import org.springframework.http.HttpStatus;

public class ConflitoException extends BaseHttpMappedException {

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }

    public ConflitoException(String message) {
        super(message);
    }

}
