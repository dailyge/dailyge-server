package project.dailyge.app.response;

import lombok.Getter;
import project.dailyge.app.codeandmessage.CodeAndMessage;

@Getter
public class ErrorResponse {

    private int code;
    private String message;

    private ErrorResponse() {
    }

    private ErrorResponse(final CodeAndMessage codeAndMessage) {
        this.code = codeAndMessage.code();
        this.message = codeAndMessage.message();
    }

    public static ErrorResponse from(final CodeAndMessage codeAndMessage) {
        return new ErrorResponse(codeAndMessage);
    }
}
