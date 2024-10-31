package project.dailyge.app.response;

import project.dailyge.app.codeandmessage.CodeAndMessage;

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

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
