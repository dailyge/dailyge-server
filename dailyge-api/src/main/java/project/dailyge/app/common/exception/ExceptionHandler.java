package project.dailyge.app.common.exception;

import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.validation.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import project.dailyge.app.common.codeandmessage.*;
import project.dailyge.app.common.response.*;

import java.util.*;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    private static final String FIELD = "field: ";
    private static final String REJECTED_VALUE = ", rejectedValue: ";
    private static final String MESSAGE = ", message: ";

    @org.springframework.web.bind.annotation.ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorResponse> resolveCommonException(CommonException exception) {
        CodeAndMessage codeAndMessage = exception.getCodeAndMessage();
        log.error("error: {}", exception.getDetailMessage());
        return ResponseEntity.status(codeAndMessage.code())
            .body(ErrorResponse.from(codeAndMessage));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> resolveMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        StringBuilder sb = new StringBuilder();
        if (exception != null) {
            Object targetObj = exception.getBindingResult().getTarget();
            List<FieldError> allErrors = exception.getBindingResult().getFieldErrors();
            for (FieldError error : allErrors) {
                sb.append(FIELD)
                    .append(error.getField())
                    .append(REJECTED_VALUE)
                    .append(error.getRejectedValue())
                    .append(MESSAGE)
                    .append(error.getDefaultMessage());
            }
            log.error("request: {}", targetObj);
        }
        log.error("errors: [{}]", sb);
        CodeAndMessage codeAndMessage = CommonCodeAndMessage.INVALID_PARAMETERS;
        return ResponseEntity.status(codeAndMessage.code())
            .body(ErrorResponse.from(codeAndMessage));
    }
}
