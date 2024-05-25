package project.dailyge.app.common.response;

import org.springframework.http.*;
import project.dailyge.app.common.codeandmessage.*;

public class ApiResponse<T> extends ResponseEntity<Payload<T>> {

    public ApiResponse(
        CodeAndMessage codeAndMessage,
        T data
    ) {
        super(new Payload<>(codeAndMessage, data), HttpStatus.valueOf(codeAndMessage.code()));
    }

    public static <T> ApiResponse<T> from(CodeAndMessage codeAndMessage) {
        return new ApiResponse<>(
            codeAndMessage,
            null
        );
    }

    public static <T> ApiResponse<T> from(
        CodeAndMessage codeAndMessage,
        T data
    ) {
        return new ApiResponse<>(
            codeAndMessage,
            data
        );
    }
}
