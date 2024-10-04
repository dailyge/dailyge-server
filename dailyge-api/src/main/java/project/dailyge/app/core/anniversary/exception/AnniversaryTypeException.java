package project.dailyge.app.core.anniversary.exception;

import lombok.Getter;
import project.dailyge.app.codeandmessage.CodeAndMessage;
import project.dailyge.app.common.exception.BusinessException;
import static project.dailyge.app.core.anniversary.exception.AnniversaryCodeAndMessage.ANNIVERSARY_NOT_FOUND;
import static project.dailyge.app.core.anniversary.exception.AnniversaryCodeAndMessage.ANNIVERSARY_UN_RESOLVED_EXCEPTION;
import static project.dailyge.app.core.anniversary.exception.AnniversaryCodeAndMessage.DUPLICATED_ANNIVERSARY;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_UN_RESOLVED_EXCEPTION;

import java.util.HashMap;
import java.util.Map;

@Getter
public sealed class AnniversaryTypeException extends BusinessException {

    private static final Map<CodeAndMessage, AnniversaryTypeException> factory = new HashMap<>();

    private AnniversaryTypeException(final CodeAndMessage codeAndMessage) {
        super(codeAndMessage);
    }

    static {
        factory.put(DUPLICATED_ANNIVERSARY, new AnniversaryDuplicatedException(DUPLICATED_ANNIVERSARY));
        factory.put(ANNIVERSARY_NOT_FOUND, new AnniversaryNotFoundException(ANNIVERSARY_NOT_FOUND));
        factory.put(ANNIVERSARY_UN_RESOLVED_EXCEPTION, new AnniversaryUnResolvedException(ANNIVERSARY_UN_RESOLVED_EXCEPTION));
    }

    public static AnniversaryTypeException from(final AnniversaryCodeAndMessage codeAndMessage) {
        return getException(codeAndMessage);
    }

    public static AnniversaryTypeException from(
        final String detailMessage,
        final AnniversaryCodeAndMessage codeAndMessage
    ) {
        final AnniversaryTypeException anniversaryTypeException = getException(codeAndMessage);
        if (detailMessage != null) {
            anniversaryTypeException.addDetailMessage(detailMessage);
        }
        return anniversaryTypeException;
    }

    private static AnniversaryTypeException getException(final CodeAndMessage codeAndMessage) {
        final AnniversaryTypeException findAnniversaryTypeException = factory.get(codeAndMessage);
        if (findAnniversaryTypeException != null) {
            return findAnniversaryTypeException;
        }
        return factory.get(TASK_UN_RESOLVED_EXCEPTION);
    }

    @Override
    protected void addDetailMessage(final String detailMessage) {
        super.addDetailMessage(detailMessage);
    }

    private static final class AnniversaryDuplicatedException extends AnniversaryTypeException {
        private AnniversaryDuplicatedException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class AnniversaryNotFoundException extends AnniversaryTypeException {
        private AnniversaryNotFoundException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class AnniversaryUnResolvedException extends AnniversaryTypeException {
        private AnniversaryUnResolvedException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }
}
