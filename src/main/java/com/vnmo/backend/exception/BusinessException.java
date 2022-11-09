package com.vnmo.backend.exception;

import lombok.Data;

@Data
public class BusinessException extends RuntimeException{

    private ExceptionCode exceptionCode;

    private Object[] args;

    public BusinessException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public BusinessException(ExceptionCode exceptionCode, Object[] args) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
        this.args = args;
    }
}
