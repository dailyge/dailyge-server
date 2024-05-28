package project.dailyge.app.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.dailyge.app.common.codeandmessage.CodeAndMessage;

public class ApiResponse<T> extends ResponseEntity<Payload<T>> {

    public ApiResponse(
        final CodeAndMessage codeAndMessage,
        final T data
    ) {
        super(new Payload<>(codeAndMessage, data), HttpStatus.valueOf(codeAndMessage.code()));
    }

    public static <T> ApiResponse<T> from(final CodeAndMessage codeAndMessage) {
        return new ApiResponse<>(
            codeAndMessage,
            null
        );
    }

    public static <T> ApiResponse<T> from(
        final CodeAndMessage codeAndMessage,
        final T data
    ) {
        return new ApiResponse<>(
            codeAndMessage,
            data
        );
    }
}
