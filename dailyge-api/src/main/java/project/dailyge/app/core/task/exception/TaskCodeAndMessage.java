package project.dailyge.app.core.task.exception;

import lombok.Getter;
import project.dailyge.app.common.codeandmessage.CodeAndMessage;

@Getter
public enum TaskCodeAndMessage implements CodeAndMessage {
    TASK_NOT_FOUND(404, "할 일을 찾을 수 없습니다.");

    private final int code;
    private final String message;

    TaskCodeAndMessage(
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
