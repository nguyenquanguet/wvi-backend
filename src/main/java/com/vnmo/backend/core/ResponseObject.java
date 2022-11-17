package com.vnmo.backend.core;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import static com.vnmo.backend.constant.ConstantValue.RESPONSE_GLOBAL_ERROR;
import static com.vnmo.backend.constant.ConstantValue.RESPONSE_SUCCESS;

@Data
@Builder
public class ResponseObject<T> {

    private int result;

    private String code;

    private String message;

    private T data;

    public static <T> ResponseObject<T> of(T data) {
        return new ResponseObject<>(1, "200", "success", data);
    }

    public static <T> ResponseObject<T> of(T data, Integer result, String message) {
        return new ResponseObject<>(result, RESPONSE_GLOBAL_ERROR, message, data);
    }

    public static <T> ResponseObject<T> error(T data, String message) {
        return new ResponseObject<>(1, RESPONSE_SUCCESS, message, data);
    }

    public static ResponseEntity<?> response(Object object) {
        return ResponseEntity.ok(ResponseObject.of(object));
    }
}
