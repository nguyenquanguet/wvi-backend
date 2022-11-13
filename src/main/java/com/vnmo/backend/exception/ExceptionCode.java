package com.vnmo.backend.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    ERROR_INVALID_PARAM("400", "error.invalid.param"),
    ERROR_WRONG_PASSWORD("402", "error.wrong.password"),
    ERROR_USERNAME_NOT_FOUND("403", "error.username.not.found"),
    INTERNAL_SERVER_ERROR("500", "internal.server.error"),
    ERROR_AP_ID_NOT_FOUND("404", "error.ap.id.not.found"),
    ERROR_INDICATOR_ID_NOT_FOUND("405", "error.indicator.id.not.found"),
    ERROR_AP_NOT_HAVE_INDICATOR("406","error.ap.not.have.indicator" ),
    ERROR_MIS_DATA_NOT_FOUND("407", "error.mis.data.not.found"),
    ERROR_USER_NOT_HAVE_PERMISSION("403","error.user.not.have.permission" ),
    ERROR_DATA_EXISTED("405", "error.data.existed" ),
    ERROR_UTIL_NOT_FOUND("404", "error.util.not.found" );

    private final String code;
    private final String message;

    ExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
