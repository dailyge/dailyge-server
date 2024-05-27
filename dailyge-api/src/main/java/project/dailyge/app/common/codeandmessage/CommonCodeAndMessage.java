package project.dailyge.app.common.codeandmessage;

public enum CommonCodeAndMessage implements CodeAndMessage {
    OK(200, "OK"),
    CREATED(201, "Created"),
    INVALID_PARAMETERS(400, "올바른 파라미터를 입력해주세요"),
    INVALID_USER_ID(403, "올바른 사용자 아이디가 아닙니다."),
    UN_AUTHORIZED(403, "권한이 존재하지 않습니다."),
    INTERNAL_SERVER_ERROR(500, "서버 내부 입니다.");

    private final int code;
    private final String message;

    CommonCodeAndMessage(
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
