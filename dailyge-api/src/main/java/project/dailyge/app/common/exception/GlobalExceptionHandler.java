package project.dailyge.app.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import project.dailyge.app.codeandmessage.CodeAndMessage;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_REQUEST;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INVALID_PARAMETERS;
import project.dailyge.app.response.ErrorResponse;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String TRACE_ID = "traceId";

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> resolveIllegalArgumentException(final IllegalArgumentException exception) {
        final CodeAndMessage codeAndMessage = BAD_REQUEST;
        writeLog(codeAndMessage, exception);
        return ResponseEntity.status(codeAndMessage.code())
            .body(ErrorResponse.from(codeAndMessage));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> resolveMethodArgumentTypeMismatchException(
        final MethodArgumentTypeMismatchException exception
    ) {
        final CodeAndMessage codeAndMessage = BAD_REQUEST;
        writeLog(codeAndMessage, exception);
        return ResponseEntity.status(codeAndMessage.code())
            .body(ErrorResponse.from(codeAndMessage));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> resolveIllegalStateException(final IllegalStateException exception) {
        final CodeAndMessage codeAndMessage = INTERNAL_SERVER_ERROR;
        writeLog(codeAndMessage, exception);
        return ResponseEntity.status(codeAndMessage.code())
            .body(ErrorResponse.from(codeAndMessage));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> resolveBusinessException(final BusinessException exception) {
        writeLog(exception.getCodeAndMessage(), exception);
        return ResponseEntity.status(exception.getCode())
            .body(ErrorResponse.from(exception.getCodeAndMessage()));
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorResponse> resolveCommonException(final CommonException exception) {
        writeLog(exception.getCodeAndMessage(), exception);
        return ResponseEntity.status(exception.getCode())
            .body(ErrorResponse.from(exception.getCodeAndMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> resolveMethodArgumentNotValidException(
        final MethodArgumentNotValidException exception
    ) {
        writeLog(INVALID_PARAMETERS, exception);
        return ResponseEntity.status(INVALID_PARAMETERS.code())
            .body(ErrorResponse.from(INVALID_PARAMETERS));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> resolveHandlerMethodValidationException(
        final HandlerMethodValidationException exception
    ) {
        writeLog(INVALID_PARAMETERS, exception);
        return ResponseEntity.status(INVALID_PARAMETERS.code())
            .body(ErrorResponse.from(INVALID_PARAMETERS));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> resolveServerError(final Exception exception) {
        writeLog(INTERNAL_SERVER_ERROR, exception);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR.code())
            .body(ErrorResponse.from(INTERNAL_SERVER_ERROR));
    }

    private void writeLog(
        final CodeAndMessage codeAndMessage,
        final Exception exception
    ) {
        final int code = codeAndMessage.code();
        final String message = codeAndMessage.message();
        final String detailMessage = exception.getMessage();
        final LocalDateTime time = LocalDateTime.now();
        log.error(
            "{\"traceId\": \"{}\", \"code\": \"{}\", \"message\": \"{}\", \"detailMessage\": \"{}\", \"time\": \"{}\"}",
            MDC.get(TRACE_ID),
            code,
            message,
            detailMessage,
            time
        );
    }
}
