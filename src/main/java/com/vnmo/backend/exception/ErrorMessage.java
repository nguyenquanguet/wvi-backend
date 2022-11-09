package com.vnmo.backend.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessage {

    private int result;

    private String code;

    private String message;

    private Object data;

    public static ErrorMessage getError(String code, String message){
        return new ErrorMessage(0, code, message, null);
    }
}
