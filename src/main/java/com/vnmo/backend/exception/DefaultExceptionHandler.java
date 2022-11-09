package com.vnmo.backend.exception;

import com.vnmo.backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class DefaultExceptionHandler {

    private final MessageService messageService;

    private final String ERROR = "Error: ";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        log.error(ERROR, e);
        String code = ExceptionCode.INTERNAL_SERVER_ERROR.getCode();
        String message = messageService.getMessage(ExceptionCode.INTERNAL_SERVER_ERROR.getMessage(), null, LocaleContextHolder.getLocale());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code(code)
                .message(message)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorMessage> handleBusinessException(BusinessException e) {
        log.error(ERROR, e);
        String code = e.getExceptionCode().getCode();
        String message = messageService.getMessage(e.getExceptionCode().getMessage(), e.getArgs(), LocaleContextHolder.getLocale());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code(code)
                .message(message)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(ValidationExceptionHandle.class)
    public ResponseEntity<ErrorMessage> handleValidationException(ValidationExceptionHandle exception) {
        log.error(ERROR, exception);
        String code = ExceptionCode.ERROR_INVALID_PARAM.getCode();
        String message = getValidationMessage(exception);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code(code)
                .message(message)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    private String getValidationMessage(ValidationExceptionHandle exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        if (!CollectionUtils.isEmpty(fieldErrors)) {
            return fieldErrors.get(0).getDefaultMessage();
        }
        return null;
    }

}
