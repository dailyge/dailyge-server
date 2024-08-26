package project.dailyge.app.codeandmessage;

public enum CommonCodeAndMessage implements CodeAndMessage {
    OK(200, "OK"),
    CREATED(201, "Created"),
    NO_CONTENT(204, "No-Content"),
    FOUND(302, "Temporarily-Redirect"),
    BAD_REQUEST(400, "올바른 파라미터를 입력해주세요"),
    INVALID_PARAMETERS(400, "올바른 파라미터를 입력해주세요"),
    INVALID_USER_ID(403, "올바른 사용자 아이디가 아닙니다."),
    INVALID_USER_TOKEN(403, "유효하지 않는 사용자 토큰입니다."),
    UN_AUTHORIZED(403, "권한이 존재하지 않습니다."),
    INVALID_URL(404, "올바르지 않은 URL 입니다."),
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류입니다."),
    DATA_ACCESS_EXCEPTION(500, "데이터베이스와 통신하는 과정에서 오류가 발생했습니다"),
    BAD_GATEWAY(502, "외부 서버와 통신하는 과정에서 오류가 발생했습니다."),
    SERVICE_UNAVAILABLE(503, "외부 서버가 응답할 수 없는 상태입니다."),
    GATEWAY_TIMEOUT(504, "외부 서버와 통신하는 과정에서 응답시간이 초과되었습니다.");

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
