package com.creditas.ce_loan_ms.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class DataBaseException extends BaseException {

    public DataBaseException(String message, Object... args) {
        super(message, HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), args);
    }

    public DataBaseException(String message) {
        super(message, HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()));
    }

    public DataBaseException(String message, Throwable e) {
        super(message, HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), e);
    }
}
