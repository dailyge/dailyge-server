package project.dailyge.app.common.response;

import lombok.*;
import project.dailyge.app.common.codeandmessage.*;

@Getter
public class ErrorResponse {

    private int code;
    private String message;

    private ErrorResponse() {
    }

    private ErrorResponse(CodeAndMessage codeAndMessage) {
        this.code = codeAndMessage.code();
        this.message = codeAndMessage.message();
    }

    public static ErrorResponse from(CodeAndMessage codeAndMessage) {
        return new ErrorResponse(codeAndMessage);
    }
}
