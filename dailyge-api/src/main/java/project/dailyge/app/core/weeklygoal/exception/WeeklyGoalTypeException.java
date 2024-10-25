package project.dailyge.app.core.weeklygoal.exception;

import project.dailyge.app.common.exception.BusinessException;

import java.util.HashMap;
import java.util.Map;

import static project.dailyge.app.core.weeklygoal.exception.WeeklyGoalCodeAndMessage.WEEKLY_GOAL_NOT_FOUND;
import static project.dailyge.app.core.weeklygoal.exception.WeeklyGoalCodeAndMessage.WEEKLY_GOAL_UN_RESOLVED_EXCEPTION;

public sealed class WeeklyGoalTypeException extends BusinessException {

    private static final Map<WeeklyGoalCodeAndMessage, WeeklyGoalTypeException> factory = new HashMap<>();

    private WeeklyGoalTypeException(final WeeklyGoalCodeAndMessage codeAndMessage) {
        super(codeAndMessage);
    }

    static {
        factory.put(WEEKLY_GOAL_NOT_FOUND, new WeeklyGoalNotFoundException(WEEKLY_GOAL_NOT_FOUND));
        factory.put(WEEKLY_GOAL_UN_RESOLVED_EXCEPTION, new WeeklyGoalUnResolvedException(WEEKLY_GOAL_UN_RESOLVED_EXCEPTION));
    }

    public static WeeklyGoalTypeException from(final WeeklyGoalCodeAndMessage codeAndMessage) {
        return getException(codeAndMessage);
    }

    public static WeeklyGoalTypeException from(
        final String detailMessage,
        final WeeklyGoalCodeAndMessage codeAndMessage
    ) {
        final WeeklyGoalTypeException taskTypeException = getException(codeAndMessage);
        if (detailMessage != null) {
            taskTypeException.addDetailMessage(detailMessage);
        }
        return taskTypeException;
    }

    private static WeeklyGoalTypeException getException(final WeeklyGoalCodeAndMessage codeAndMessage) {
        final WeeklyGoalTypeException findTaskTypeException = factory.get(codeAndMessage);
        if (findTaskTypeException != null) {
            return findTaskTypeException;
        }
        return factory.get(WEEKLY_GOAL_UN_RESOLVED_EXCEPTION);
    }

    @Override
    protected void addDetailMessage(final String detailMessage) {
        super.addDetailMessage(detailMessage);
    }

    private static final class WeeklyGoalNotFoundException extends WeeklyGoalTypeException {
        private WeeklyGoalNotFoundException(final WeeklyGoalCodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class WeeklyGoalUnResolvedException extends WeeklyGoalTypeException {
        private WeeklyGoalUnResolvedException(final WeeklyGoalCodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }
}
