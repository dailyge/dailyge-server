package project.dailyge.app.core.note.exception;

import project.dailyge.app.codeandmessage.CodeAndMessage;
import project.dailyge.app.common.exception.BusinessException;
import static project.dailyge.app.core.note.exception.NoteCodeAndMessage.NOTE_NOT_FOUND;
import static project.dailyge.app.core.note.exception.NoteCodeAndMessage.NOTE_UN_RESOLVED_EXCEPTION;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_UN_RESOLVED_EXCEPTION;

import java.util.HashMap;
import java.util.Map;

public sealed class NoteTypeException extends BusinessException {

    private static final Map<CodeAndMessage, NoteTypeException> factory = new HashMap<>();

    private NoteTypeException(final CodeAndMessage codeAndMessage) {
        super(codeAndMessage);
    }

    static {
        factory.put(NOTE_NOT_FOUND, new NoteNotFoundException(NOTE_NOT_FOUND));
        factory.put(NOTE_UN_RESOLVED_EXCEPTION, new NoteUnResolvedException(NOTE_UN_RESOLVED_EXCEPTION));
    }

    public static NoteTypeException from(final CodeAndMessage codeAndMessage) {
        return getException(codeAndMessage);
    }

    public static NoteTypeException from(
        final String detailMessage,
        final CodeAndMessage codeAndMessage
    ) {
        final NoteTypeException taskTypeException = getException(codeAndMessage);
        if (detailMessage != null) {
            taskTypeException.addDetailMessage(detailMessage);
        }
        return taskTypeException;
    }

    private static NoteTypeException getException(final CodeAndMessage codeAndMessage) {
        final NoteTypeException findTaskTypeException = factory.get(codeAndMessage);
        if (findTaskTypeException != null) {
            return findTaskTypeException;
        }
        return factory.get(TASK_UN_RESOLVED_EXCEPTION);
    }

    private static final class NoteNotFoundException extends NoteTypeException {
        private NoteNotFoundException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class NoteUnResolvedException extends NoteTypeException {
        private NoteUnResolvedException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }
}
