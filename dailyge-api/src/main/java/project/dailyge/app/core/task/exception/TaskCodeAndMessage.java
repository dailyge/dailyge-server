package project.dailyge.app.core.task.exception;

import lombok.Getter;
import project.dailyge.app.common.codeandmessage.CodeAndMessage;

@Getter
public enum TaskCodeAndMessage implements CodeAndMessage {
    MONTHLY_TASK_EXISTS(400, "올 해 월간 일정표가 존재합니다. 새로운 일정표를 생성할 수 없습니다."),
    TOO_MANY_TASKS(400, "등록 가능한 할 일 개수를 초과했습니다."),
    TASK_REGISTERING(400, "할 일을 중복 등록할 수 없습니다."),
    MONTHLY_TASK_NOT_EXISTS(404, "월간 일정표가 존재하지 않습니다."),
    TASK_NOT_FOUND(404, "할 일을 찾을 수 없습니다."),
    TASK_UN_RESOLVED_EXCEPTION(500, "작업이 실패했습니다. 진행중이던 작업을 확인해주세요.");

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
