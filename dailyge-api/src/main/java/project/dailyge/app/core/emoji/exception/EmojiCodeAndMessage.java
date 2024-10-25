package project.dailyge.app.core.emoji.exception;

import lombok.Getter;
import project.dailyge.app.codeandmessage.CodeAndMessage;

@Getter
public enum EmojiCodeAndMessage implements CodeAndMessage {
    EMOJI_NOT_FOUND(404, "이모티콘이 존재하지 않습니다."),
    EMOJI_UN_RESOLVED_EXCEPTION(500, "작업이 실패했습니다. 진행중인 작업을 확인해주세요.");

    private final int code;
    private final String message;

    EmojiCodeAndMessage(
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
