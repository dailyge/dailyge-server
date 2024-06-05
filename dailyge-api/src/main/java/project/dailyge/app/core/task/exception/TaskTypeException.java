package project.dailyge.app.core.task.exception;

import project.dailyge.app.common.codeandmessage.CodeAndMessage;
import project.dailyge.app.common.exception.BusinessException;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_NOT_FOUND;

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
        if (TASK_NOT_FOUND.equals(codeAndMessage)) {
            throw new TaskNotFoundException(codeAndMessage);
        }
        return new TaskUnResolvedException(codeAndMessage);
    }

    private static final class TaskNotFoundException extends TaskTypeException {
        private TaskNotFoundException(final CodeAndMessage codeAndMessage) {
            super(TASK_NOT_FOUND_MESSAGE, codeAndMessage);
        }
    }

    private static final class TaskUnResolvedException extends TaskTypeException {
        private TaskUnResolvedException(final CodeAndMessage codeAndMessage) {
            super(TASK_UN_RESOLVED_MESSAGE, codeAndMessage);
        }
    }
}
