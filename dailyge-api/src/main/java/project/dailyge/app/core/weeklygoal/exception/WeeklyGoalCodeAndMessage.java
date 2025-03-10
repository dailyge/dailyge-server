package project.dailyge.app.core.weeklygoal.exception;

import project.dailyge.app.codeandmessage.CodeAndMessage;

public enum WeeklyGoalCodeAndMessage implements CodeAndMessage {

    WEEKLY_GOAL_NOT_FOUND(404, "주간 목표를 찾을 수 없습니다."),
    WEEKLY_GOAL_UN_RESOLVED_EXCEPTION(500, "작업이 실패했습니다. 진행중인 작업을 확인해주세요.");

    private final int code;
    private final String message;

    WeeklyGoalCodeAndMessage(
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
