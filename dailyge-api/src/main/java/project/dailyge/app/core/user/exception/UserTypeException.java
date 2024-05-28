package project.dailyge.app.core.user.exception;

import project.dailyge.app.common.codeandmessage.CodeAndMessage;
import project.dailyge.app.common.exception.BusinessException;

public sealed class UserTypeException extends BusinessException {

    private static final String UN_RESOLVED_MESSAGE = "해결되지 못한 사용자 예외입니다.";

    private UserTypeException(
        final String detailMessage,
        final CodeAndMessage codeAndMessage
    ) {
        super(detailMessage, codeAndMessage);
    }

    public static UserTypeException from(final CodeAndMessage codeAndMessage) {
        final UserCodeAndMessage userCodeAndMessage = (UserCodeAndMessage) codeAndMessage;
        switch (userCodeAndMessage) {
            case USER_NOT_FOUND:
                return new UserNotFoundException(null, codeAndMessage);
            default:
                return new UserUnResolvedException(codeAndMessage);
        }
    }

    private static final class UserNotFoundException extends UserTypeException {
        public UserNotFoundException(
            final String detailMessage,
            final CodeAndMessage codeAndMessage
        ) {
            super(detailMessage, codeAndMessage);
        }
    }

    private static final class UserUnResolvedException extends UserTypeException {
        public UserUnResolvedException(
            final CodeAndMessage codeAndMessage
        ) {
            super(UN_RESOLVED_MESSAGE, codeAndMessage);
        }
    }
}
