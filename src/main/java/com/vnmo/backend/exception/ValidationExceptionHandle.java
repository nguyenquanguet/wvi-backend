package com.vnmo.backend.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BindingResult;

@Getter
@Setter
public class ValidationExceptionHandle extends RuntimeException {

    private BindingResult bindingResult;

    public ValidationExceptionHandle(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }
}