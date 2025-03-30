package com.hackaton.controller.handlers;

import com.hackaton.exception.BaseHttpMappedException;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@Hidden
@RestControllerAdvice
public class RetornoSUSExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RetornoSUSExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity handleBadRequest(MethodArgumentNotValidException ex) {
		var errors = ex.getFieldErrors();
		return ResponseEntity.badRequest().body(errors.stream().map(FieldValidationError::new).toList());
	}

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleBadRequest2(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(new Error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity notFoundException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(new Error(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handleAccessDenied() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Error(HttpStatus.UNAUTHORIZED, "Access denied"));
    }

    @ExceptionHandler(BaseHttpMappedException.class)
    public ResponseEntity handleException(BaseHttpMappedException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(new Error(ex.getHttpStatus(), ex.getLocalizedMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception ex) {
        log.error("Erro Interno - ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("Erro Interno, vefirique os logs da aplicação"));
    }

	private record Error (Integer errorCode, String message) {
		public Error(HttpStatus httpStatus, String message) { this(httpStatus.value(), message); }
		public Error(String message) {	this(HttpStatus.INTERNAL_SERVER_ERROR.value(), message); }
	}
	
	private record FieldValidationError(String field, String message) {
		public FieldValidationError(FieldError fe) { this(fe.getField(), fe.getDefaultMessage()); }
	}
}