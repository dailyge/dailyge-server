package project.dailyge.app.core.monthly_goal.exception;

import lombok.Getter;
import project.dailyge.app.codeandmessage.CodeAndMessage;
import project.dailyge.app.common.exception.BusinessException;
import static project.dailyge.app.core.monthly_goal.exception.MonthlyGoalCodeAndMessage.MONTHLY_GOAL_NOT_FOUND;
import static project.dailyge.app.core.monthly_goal.exception.MonthlyGoalCodeAndMessage.MONTHLY_GOAL_UN_RESOLVED_EXCEPTION;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_UN_RESOLVED_EXCEPTION;

import java.util.HashMap;
import java.util.Map;

@Getter
public sealed class MonthlyGoalTypeException extends BusinessException {

    private static final Map<CodeAndMessage, MonthlyGoalTypeException> factory = new HashMap<>();

    private MonthlyGoalTypeException(final CodeAndMessage codeAndMessage) {
        super(codeAndMessage);
    }

    static {
        factory.put(MONTHLY_GOAL_NOT_FOUND, new MonthlyGoalNotFoundException(MONTHLY_GOAL_NOT_FOUND));
        factory.put(MONTHLY_GOAL_UN_RESOLVED_EXCEPTION, new MonthlyGoalUnResolvedException(MONTHLY_GOAL_UN_RESOLVED_EXCEPTION));
    }

    public static MonthlyGoalTypeException from(final MonthlyGoalCodeAndMessage codeAndMessage) {
        return getException(codeAndMessage);
    }

    public static MonthlyGoalTypeException from(
        final String detailMessage,
        final MonthlyGoalCodeAndMessage codeAndMessage
    ) {
        final MonthlyGoalTypeException taskTypeException = getException(codeAndMessage);
        if (detailMessage != null) {
            taskTypeException.addDetailMessage(detailMessage);
        }
        return taskTypeException;
    }

    private static MonthlyGoalTypeException getException(final CodeAndMessage codeAndMessage) {
        final MonthlyGoalTypeException findTaskTypeException = factory.get(codeAndMessage);
        if (findTaskTypeException != null) {
            return findTaskTypeException;
        }
        return factory.get(TASK_UN_RESOLVED_EXCEPTION);
    }

    @Override
    protected void addDetailMessage(final String detailMessage) {
        super.addDetailMessage(detailMessage);
    }

    private static final class MonthlyGoalNotFoundException extends MonthlyGoalTypeException {
        private MonthlyGoalNotFoundException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class MonthlyGoalUnResolvedException extends MonthlyGoalTypeException {
        private MonthlyGoalUnResolvedException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }
}
