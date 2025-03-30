package com.hackaton.exception;

import org.springframework.http.HttpStatus;

public class ValidacaoException extends BaseHttpMappedException {

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    public ValidacaoException(String message) {
        super(message);
    }

}
