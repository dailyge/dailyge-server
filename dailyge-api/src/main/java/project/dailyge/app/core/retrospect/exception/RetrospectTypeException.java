package project.dailyge.app.core.retrospect.exception;

import project.dailyge.app.codeandmessage.CodeAndMessage;
import project.dailyge.app.common.exception.BusinessException;
import static project.dailyge.app.core.retrospect.exception.RetrospectCodeAndMessage.DUPLICATED_RETROSPECT;
import static project.dailyge.app.core.retrospect.exception.RetrospectCodeAndMessage.RETROSPECT_NOT_FOUND;
import static project.dailyge.app.core.retrospect.exception.RetrospectCodeAndMessage.RETROSPECT_UN_RESOLVED_EXCEPTION;

import java.util.HashMap;
import java.util.Map;

public sealed class RetrospectTypeException extends BusinessException {

    private static final Map<CodeAndMessage, RetrospectTypeException> factory = new HashMap<>();

    private RetrospectTypeException(final CodeAndMessage codeAndMessage) {
        super(codeAndMessage);
    }

    static {
        factory.put(DUPLICATED_RETROSPECT, new RetrospectDuplicatedException(DUPLICATED_RETROSPECT));
        factory.put(RETROSPECT_NOT_FOUND, new RetrospectNotFoundException(RETROSPECT_NOT_FOUND));
        factory.put(RETROSPECT_UN_RESOLVED_EXCEPTION, new RetrospectUnResolvedException(RETROSPECT_UN_RESOLVED_EXCEPTION));
    }

    public static RetrospectTypeException from(final RetrospectCodeAndMessage codeAndMessage) {
        return getException(codeAndMessage);
    }

    public static RetrospectTypeException from(
        final String detailMessage,
        final RetrospectCodeAndMessage codeAndMessage
    ) {
        final RetrospectTypeException retrospectTypeException = getException(codeAndMessage);
        if (detailMessage != null) {
            retrospectTypeException.addDetailMessage(detailMessage);
        }
        return retrospectTypeException;
    }

    private static RetrospectTypeException getException(final CodeAndMessage codeAndMessage) {
        final RetrospectTypeException findRetrospectTypeException = factory.get(codeAndMessage);
        if (findRetrospectTypeException != null) {
            return findRetrospectTypeException;
        }
        return factory.get(RETROSPECT_UN_RESOLVED_EXCEPTION);
    }

    @Override
    protected void addDetailMessage(final String detailMessage) {
        super.addDetailMessage(detailMessage);
    }

    private static final class RetrospectDuplicatedException extends RetrospectTypeException {
        private RetrospectDuplicatedException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class RetrospectNotFoundException extends RetrospectTypeException {
        private RetrospectNotFoundException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class RetrospectUnResolvedException extends RetrospectTypeException {
        private RetrospectUnResolvedException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }
}
