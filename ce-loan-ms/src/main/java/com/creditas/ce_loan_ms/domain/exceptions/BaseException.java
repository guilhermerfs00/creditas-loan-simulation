package com.creditas.ce_loan_ms.domain.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.io.Serializable;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
public class BaseException extends RuntimeException implements Serializable {

    private final HttpStatusCode status;
    private final Object[] args;

    public BaseException(String message) {
        super(message);
        this.status = INTERNAL_SERVER_ERROR;
        this.args = null;
    }

    public BaseException(String message, Object[] args) {
        super(message);
        this.status = INTERNAL_SERVER_ERROR;
        this.args = args;
    }

    public BaseException(String message, HttpStatusCode status, Object[] args) {
        super(message);
        this.status = status;
        this.args = args;
    }

    public BaseException(String message, HttpStatusCode status) {
        super(message);
        this.status = status;
        this.args = null;
    }

    public BaseException(String message, HttpStatusCode status, Throwable cause, Object[] args) {
        super(message, cause);
        this.status = status;
        this.args = args;
    }

    public BaseException(String message, HttpStatusCode status, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.args = null;
    }

    public BaseException(String message, Throwable cause, Object[] args) {
        super(message, cause);
        this.status = INTERNAL_SERVER_ERROR;
        this.args = args;
    }
}