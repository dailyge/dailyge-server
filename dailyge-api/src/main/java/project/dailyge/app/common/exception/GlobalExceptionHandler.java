package project.dailyge.app.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.dailyge.app.codeandmessage.CodeAndMessage;
import project.dailyge.app.codeandmessage.CommonCodeAndMessage;
import project.dailyge.app.response.ErrorResponse;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String FIELD = "field: ";
    private static final String REJECTED_VALUE = ", rejectedValue: ";
    private static final String MESSAGE = ", message: ";

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> resolveIllegalArgumentException(final IllegalArgumentException exception) {
        final CodeAndMessage codeAndMessage = CommonCodeAndMessage.BAD_REQUEST;
        log.error("error: {}, {}", MDC.get("requestId"), exception.getMessage());
        return ResponseEntity.status(codeAndMessage.code())
            .body(ErrorResponse.from(codeAndMessage));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> resolveBusinessException(final BusinessException exception) {
        final CodeAndMessage codeAndMessage = exception.getCodeAndMessage();
        log.error("error: {}, {}", MDC.get("requestId"), exception.getMessage());
        return ResponseEntity.status(codeAndMessage.code())
            .body(ErrorResponse.from(codeAndMessage));
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorResponse> resolveCommonException(final CommonException exception) {
        final CodeAndMessage codeAndMessage = exception.getCodeAndMessage();
        log.error("error: {}, {}", MDC.get("requestId"), exception.getDetailMessage());
        return ResponseEntity.status(codeAndMessage.code())
            .body(ErrorResponse.from(codeAndMessage));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> resolveMethodArgumentNotValidException(
        final MethodArgumentNotValidException exception
    ) {
        final StringBuilder sb = new StringBuilder();
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
            log.error("error: {}, {}", MDC.get("requestId"), targetObj);
            final CodeAndMessage codeAndMessage = CommonCodeAndMessage.INVALID_PARAMETERS;
            return ResponseEntity.status(codeAndMessage.code())
                .body(ErrorResponse.from(codeAndMessage));
        }
        final CodeAndMessage codeAndMessage = CommonCodeAndMessage.INVALID_PARAMETERS;
        return ResponseEntity.status(codeAndMessage.code())
            .body(ErrorResponse.from(codeAndMessage));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> resolveServerError(final Exception exception) {
        log.error("error: {}, {}", MDC.get("requestId"), exception.getMessage());
        final CodeAndMessage codeAndMessage = CommonCodeAndMessage.INVALID_PARAMETERS;
        return ResponseEntity.status(codeAndMessage.code())
            .body(ErrorResponse.from(codeAndMessage));
    }
}
