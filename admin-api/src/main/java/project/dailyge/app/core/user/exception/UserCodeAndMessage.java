package project.dailyge.app.core.user.exception;

import project.dailyge.app.codeandmessage.CodeAndMessage;

public enum UserCodeAndMessage implements CodeAndMessage {

    USER_NOT_FOUND(404, "존재하지 않는 사용자 정보 입니다.");

    private final int code;
    private final String message;

    UserCodeAndMessage(
        final int code,
        final String message
    ) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
