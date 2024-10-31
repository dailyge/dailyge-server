package project.dailyge.app.core.event.exception;

import project.dailyge.app.codeandmessage.CodeAndMessage;

public enum EventCodeAndMessage implements CodeAndMessage {

    INVALID_EVENT(400, "유효하지 않은 이벤트입니다."),
    EVENT_NOT_FOUND(404, "이벤트를 찾을 수 없습니다."),
    EVENT_UN_RESOLVED_EXCEPTION(500, "이벤트 작업이 실패했습니다. 진행중인 작업을 확인해주세요.");

    private final int code;
    private final String message;

    EventCodeAndMessage(
        final int code,
        final String message
    ) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
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
