package project.dailyge.app.core.task.exception;

import project.dailyge.app.common.codeandmessage.CodeAndMessage;
import project.dailyge.app.common.exception.BusinessException;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.MONTHLY_TASK_EXISTS;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.MONTHLY_TASK_NOT_EXISTS;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_NOT_FOUND;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TOO_MANY_TASKS;

public sealed class TaskTypeException extends BusinessException {

    private static final String TASK_NOT_FOUND_MESSAGE = "할 일을 찾을 수 없습니다.";
    private static final String TASK_UN_RESOLVED_MESSAGE = "해결되지 못한 할 일 예외입니다.";

    private TaskTypeException(
        final String detailMessage,
        final CodeAndMessage codeAndMessage
    ) {
        super(detailMessage, codeAndMessage);
    }

    public static TaskTypeException from(final TaskCodeAndMessage codeAndMessage) {
        if (MONTHLY_TASK_EXISTS.equals(codeAndMessage)) {
            throw new YearPlanExistsException(codeAndMessage);
        }
        if (TOO_MANY_TASKS.equals(codeAndMessage)) {
            throw new TooManyTasksException(codeAndMessage);
        }
        if (MONTHLY_TASK_NOT_EXISTS.equals(codeAndMessage)) {
            throw new YearPlanNotExistsException(codeAndMessage);
        }
        if (TASK_NOT_FOUND.equals(codeAndMessage)) {
            throw new TaskNotFoundException(codeAndMessage);
        }
        return new TaskUnResolvedException(codeAndMessage);
    }

    public static TaskTypeException from(
        final String detailMessage,
        final TaskCodeAndMessage codeAndMessage
    ) {
        if (MONTHLY_TASK_EXISTS.equals(codeAndMessage)) {
            throw new YearPlanExistsException(detailMessage, codeAndMessage);
        }
        if (TOO_MANY_TASKS.equals(codeAndMessage)) {
            throw new TooManyTasksException(detailMessage, codeAndMessage);
        }
        if (MONTHLY_TASK_NOT_EXISTS.equals(codeAndMessage)) {
            throw new YearPlanNotExistsException(detailMessage, codeAndMessage);
        }
        if (TASK_NOT_FOUND.equals(codeAndMessage)) {
            throw new TaskNotFoundException(detailMessage, codeAndMessage);
        }
        return new TaskUnResolvedException(detailMessage, codeAndMessage);
    }

    private static final class YearPlanExistsException extends TaskTypeException {
        private YearPlanExistsException(final CodeAndMessage codeAndMessage) {
            super("", codeAndMessage);
        }

        private YearPlanExistsException(
            final String detailMessage,
            final CodeAndMessage codeAndMessage
        ) {
            super(detailMessage, codeAndMessage);
        }
    }

    private static final class TooManyTasksException extends TaskTypeException {
        private TooManyTasksException(final CodeAndMessage codeAndMessage) {
            super("", codeAndMessage);
        }

        private TooManyTasksException(
            final String detailMessage,
            final CodeAndMessage codeAndMessage
        ) {
            super(detailMessage, codeAndMessage);
        }
    }

    private static final class YearPlanNotExistsException extends TaskTypeException {
        private YearPlanNotExistsException(final CodeAndMessage codeAndMessage) {
            super("", codeAndMessage);
        }

        private YearPlanNotExistsException(
            final String detailMessage,
            final CodeAndMessage codeAndMessage
        ) {
            super(detailMessage, codeAndMessage);
        }
    }

    private static final class TaskNotFoundException extends TaskTypeException {
        private TaskNotFoundException(final CodeAndMessage codeAndMessage) {
            super(TASK_NOT_FOUND_MESSAGE, codeAndMessage);
        }

        private TaskNotFoundException(
            final String detailMessage,
            final CodeAndMessage codeAndMessage
        ) {
            super(detailMessage, codeAndMessage);
        }
    }

    private static final class TaskUnResolvedException extends TaskTypeException {
        private TaskUnResolvedException(final CodeAndMessage codeAndMessage) {
            super(TASK_UN_RESOLVED_MESSAGE, codeAndMessage);
        }

        private TaskUnResolvedException(
            final String detailMessage,
            final CodeAndMessage codeAndMessage
        ) {
            super(detailMessage, codeAndMessage);
        }
    }
}
