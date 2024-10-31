package project.dailyge.app.common.exception;

import project.dailyge.app.codeandmessage.CodeAndMessage;
import project.dailyge.app.codeandmessage.CommonCodeAndMessage;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_REQUEST;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.COMMON_UN_RESOLVED_EXCEPTION;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.DATA_ACCESS_EXCEPTION;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.GATEWAY_TIMEOUT;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INVALID_PARAMETERS;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INVALID_URL;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INVALID_USER_ID;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INVALID_USER_TOKEN;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.SERVICE_UNAVAILABLE;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;

import java.util.HashMap;
import java.util.Map;

public sealed class CommonException extends RuntimeException {
    private static final Map<CodeAndMessage, CommonException> factory = new HashMap<>();
    private String detailMessage;
    private final CodeAndMessage codeAndMessage;

    private CommonException(final CodeAndMessage codeAndMessage) {
        super(codeAndMessage.message());
        this.codeAndMessage = codeAndMessage;
    }

    public CommonException(
        final String detailMessage,
        final CodeAndMessage codeAndMessage
    ) {
        super(codeAndMessage.message());
        this.detailMessage = detailMessage;
        this.codeAndMessage = codeAndMessage;
    }

    static {
        initializeFactory();
    }

    private static void initializeFactory() {
        factory.put(BAD_REQUEST, new InvalidParameterException(BAD_REQUEST));
        factory.put(INVALID_PARAMETERS, new InvalidParameterException(INVALID_PARAMETERS));
        factory.put(INVALID_USER_ID, new UnAuthorizedException(INVALID_USER_ID));
        factory.put(INVALID_USER_TOKEN, new UnAuthorizedException(INVALID_USER_TOKEN));
        factory.put(UN_AUTHORIZED, new UnAuthorizedException(UN_AUTHORIZED));
        factory.put(INVALID_URL, new InvalidParameterException(INVALID_URL));
        factory.put(INTERNAL_SERVER_ERROR, new ExternalServerException(INTERNAL_SERVER_ERROR));
        factory.put(BAD_GATEWAY, new ExternalServerException(BAD_GATEWAY));
        factory.put(SERVICE_UNAVAILABLE, new ExternalServerException(SERVICE_UNAVAILABLE));
        factory.put(GATEWAY_TIMEOUT, new ExternalServerException(GATEWAY_TIMEOUT));
        factory.put(DATA_ACCESS_EXCEPTION, new DaoException(DATA_ACCESS_EXCEPTION));
        factory.put(COMMON_UN_RESOLVED_EXCEPTION, new CommoUnResolvedException(COMMON_UN_RESOLVED_EXCEPTION));
    }

    public static CommonException from(final CommonCodeAndMessage codeAndMessage) {
        return getException(codeAndMessage);
    }

    public static CommonException from(
        final String detailMessage,
        final CommonCodeAndMessage codeAndMessage
    ) {
        final CommonException commonException = getException(codeAndMessage);
        if (detailMessage != null) {
            commonException.addDetailMessage(detailMessage);
        }
        return commonException;
    }

    public int getCode() {
        return codeAndMessage.code();
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public CodeAndMessage getCodeAndMessage() {
        return codeAndMessage;
    }

    private static CommonException getException(final CodeAndMessage codeAndMessage) {
        final CommonException commonException = factory.get(codeAndMessage);
        if (commonException != null) {
            return commonException;
        }
        return factory.get(COMMON_UN_RESOLVED_EXCEPTION);
    }

    private void addDetailMessage(final String detailMessage) {
        this.detailMessage = detailMessage;
    }

    private static final class InvalidParameterException extends CommonException {
        private InvalidParameterException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class UnAuthorizedException extends CommonException {
        private UnAuthorizedException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class DaoException extends CommonException {
        private DaoException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class ExternalServerException extends CommonException {
        private ExternalServerException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class CommoUnResolvedException extends CommonException {
        public CommoUnResolvedException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }
}
