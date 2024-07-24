package project.dailyge.app.core.user.exception;

import project.dailyge.app.codeandmessage.CodeAndMessage;

public enum UserCodeAndMessage implements CodeAndMessage {

    USER_NOT_FOUND(404, "존재하지 않는 사용자 정보 입니다."),
    EMPTY_USER_ID(404, "사용자 ID가 존재하지 않습니다."),
    DUPLICATED_EMAIL(422, "이미 존재하는 이메일 계정입니다");

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
