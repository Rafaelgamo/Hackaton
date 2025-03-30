package com.hackaton.exception;

import org.springframework.http.HttpStatus;

public class NaoEncontradoException extends BaseHttpMappedException {

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

    public NaoEncontradoException(String message) {
        super(message);
    }

}
