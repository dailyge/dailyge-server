package project.dailyge.app.core.notice.exception;

import project.dailyge.app.codeandmessage.CodeAndMessage;

public enum NoticeCodeAndMessage implements CodeAndMessage {

    NOTICE_NOT_FOUND(404, "공지사항이 존재하지 않습니다.");

    private final int code;
    private final String message;

    NoticeCodeAndMessage(
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
