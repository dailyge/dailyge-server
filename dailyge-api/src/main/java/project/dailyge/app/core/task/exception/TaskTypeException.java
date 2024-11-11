package project.dailyge.app.core.task.exception;

import project.dailyge.app.codeandmessage.CodeAndMessage;
import project.dailyge.app.common.exception.BusinessException;

import java.util.HashMap;
import java.util.Map;

import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.MONTHLY_TASK_EXISTS;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.MONTHLY_TASK_NOT_FOUND;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_NOT_FOUND;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_RECURRENCE_NOT_FOUND;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_UN_RESOLVED_EXCEPTION;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TOO_MANY_TASKS;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TOO_MANY_TASK_LABELS;

public sealed class TaskTypeException extends BusinessException {

    private static final Map<CodeAndMessage, TaskTypeException> factory = new HashMap<>();

    private TaskTypeException(final CodeAndMessage codeAndMessage) {
        super(codeAndMessage);
    }

    static {
        factory.put(TOO_MANY_TASKS, new TooManyTasksException(TOO_MANY_TASKS));
        factory.put(TOO_MANY_TASK_LABELS, new TooManyTaskLabelsException(TOO_MANY_TASK_LABELS));
        factory.put(MONTHLY_TASK_NOT_FOUND, new MonthlyPlanNotExistsException(MONTHLY_TASK_NOT_FOUND));
        factory.put(TASK_NOT_FOUND, new TaskNotFoundException(TASK_NOT_FOUND));
        factory.put(TASK_RECURRENCE_NOT_FOUND, new TaskRecurrenceNotFoundException(TASK_RECURRENCE_NOT_FOUND));
        factory.put(MONTHLY_TASK_EXISTS, new MonthlyPlanExistsException(MONTHLY_TASK_EXISTS));
        factory.put(TASK_UN_RESOLVED_EXCEPTION, new TaskUnResolvedException(TASK_UN_RESOLVED_EXCEPTION));
    }

    public static TaskTypeException from(final TaskCodeAndMessage codeAndMessage) {
        return getException(codeAndMessage);
    }

    public static TaskTypeException from(
        final String detailMessage,
        final TaskCodeAndMessage codeAndMessage
    ) {
        final TaskTypeException taskTypeException = getException(codeAndMessage);
        if (detailMessage != null) {
            taskTypeException.addDetailMessage(detailMessage);
        }
        return taskTypeException;
    }

    private static TaskTypeException getException(final CodeAndMessage codeAndMessage) {
        final TaskTypeException findTaskTypeException = factory.get(codeAndMessage);
        if (findTaskTypeException != null) {
            return findTaskTypeException;
        }
        return factory.get(TASK_UN_RESOLVED_EXCEPTION);
    }

    @Override
    protected void addDetailMessage(final String detailMessage) {
        super.addDetailMessage(detailMessage);
    }

    private static final class MonthlyPlanExistsException extends TaskTypeException {
        private MonthlyPlanExistsException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class TooManyTasksException extends TaskTypeException {
        private TooManyTasksException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class TooManyTaskLabelsException extends TaskTypeException {
        private TooManyTaskLabelsException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class MonthlyPlanNotExistsException extends TaskTypeException {
        private MonthlyPlanNotExistsException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class TaskNotFoundException extends TaskTypeException {
        private TaskNotFoundException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class TaskUnResolvedException extends TaskTypeException {
        private TaskUnResolvedException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class TaskRecurrenceNotFoundException extends TaskTypeException {
        private TaskRecurrenceNotFoundException(CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }
}
