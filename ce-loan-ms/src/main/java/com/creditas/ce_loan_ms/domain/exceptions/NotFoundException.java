package com.creditas.ce_loan_ms.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class NotFoundException extends BaseException {

    public NotFoundException(String message, Object... args) {
        super(message, HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()), args);
    }

    public NotFoundException(String message) {
        super(message, HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()));
    }

    public NotFoundException(String message, Throwable e) {
        super(message, HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), e);
    }
}