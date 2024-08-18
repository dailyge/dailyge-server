package project.dailyge.app.core.monthlygoal.exception;

import lombok.Getter;
import project.dailyge.app.codeandmessage.CodeAndMessage;

@Getter
public enum MonthlyGoalCodeAndMessage implements CodeAndMessage {
    MONTHLY_GOAL_NOT_FOUND(404, "월간 목표를 찾을 수 없습니다."),
    MONTHLY_GOAL_UN_RESOLVED_EXCEPTION(500, "작업이 실패했습니다. 진행중인 작업을 확인해주세요.");

    private final int code;
    private final String message;

    MonthlyGoalCodeAndMessage(
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
