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

    public static UserTypeException from(final UserCodeAndMessage codeAndMessage) {
        return switch (codeAndMessage) {
            case USER_NOT_FOUND -> new UserNotFoundException(null, codeAndMessage);
            case USER_EMAIL_CONFLICT -> new UserEmailConflictException(null, codeAndMessage);
            default -> new UserUnResolvedException(codeAndMessage);
        };
    }

    private static final class UserNotFoundException extends UserTypeException {
        public UserNotFoundException(
            final String detailMessage,
            final CodeAndMessage codeAndMessage
        ) {
            super(detailMessage, codeAndMessage);
        }
    }

    private static final class UserEmailConflictException extends UserTypeException {
        public UserEmailConflictException(
            final String detailMessage,
            final CodeAndMessage codeAndMessage
        ) {
            super(detailMessage, codeAndMessage);
        }
    }

    private static final class UserUnResolvedException extends UserTypeException {
        public UserUnResolvedException(final CodeAndMessage codeAndMessage) {
            super(UN_RESOLVED_MESSAGE, codeAndMessage);
        }
    }
}
