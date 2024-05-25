package project.dailyge.app.common.exception;

import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import project.dailyge.app.common.codeandmessage.*;
import project.dailyge.app.common.response.*;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorResponse> resolveCommonException(CommonException exception) {
        CodeAndMessage codeAndMessage = exception.getCodeAndMessage();
        log.error("error: {}", exception.getDetailMessage());
        return ResponseEntity.status(codeAndMessage.code())
            .body(ErrorResponse.from(codeAndMessage));
    }
}
