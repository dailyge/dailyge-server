package project.dailyge.app.core.note.exception;

import project.dailyge.app.codeandmessage.CodeAndMessage;

public enum NoteCodeAndMessage implements CodeAndMessage {
    NOTE_NOT_FOUND(404, "쪽지를 찾을 수 없습니다."),
    NOTE_UN_RESOLVED_EXCEPTION(500, "쪽지 관련 작업이 실패했습니다. 진행중인 작업을 확인해주세요.");

    private final int code;
    private final String message;

    NoteCodeAndMessage(
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
