package project.dailyge.app.core.task.exception;

import lombok.Getter;
import project.dailyge.app.codeandmessage.CodeAndMessage;

@Getter
public enum TaskCodeAndMessage implements CodeAndMessage {
    INVALID_MONTHLY_TASK(400, "올바르지 않은 Task 입니다."),
    TOO_MANY_TASKS(400, "등록 가능한 일정 개수를 초과했습니다."),
    MONTHLY_TASK_NOT_FOUND(404, "월간 일정표가 존재하지 않습니다."),
    TASK_NOT_FOUND(404, "일정을 찾을 수 없습니다."),
    MONTHLY_TASK_EXISTS(409, "올 해 월간 일정표가 존재합니다. 새로운 일정표를 생성할 수 없습니다."),
    TASK_UN_RESOLVED_EXCEPTION(500, "작업이 실패했습니다. 진행중인 작업을 확인해주세요.");

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
