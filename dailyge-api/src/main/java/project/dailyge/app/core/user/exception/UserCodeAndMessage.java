package project.dailyge.app.core.user.exception;

import project.dailyge.app.common.codeandmessage.CodeAndMessage;

public enum UserCodeAndMessage implements CodeAndMessage {

    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    USER_EMAIL_CONFLICT(409, "이미 존재하는 이메일 계정입니다");

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
