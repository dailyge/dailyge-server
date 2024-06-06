package project.dailyge.app.core.user.exception;

import project.dailyge.app.common.codeandmessage.CodeAndMessage;
import project.dailyge.app.common.exception.BusinessException;

public sealed class UserTypeException extends BusinessException {

    private static final String UN_RESOLVED_MESSAGE = "해결되지 못한 사용자 예외입니다.";
    private static final String USER_NOT_EXIST_ERROR_MESSAGE = "존재하지 않는 사용자 입니다.";
    private static final String CONFLICT_EMAIL_ERROR_MESSAGE = "이미 가입되어 있는 사용자 이메일 계정입니다";
    private static final String ACTIVE_USER_NOT_FOUND = "삭제되지 않은 사용자 정보를 찾을 수 없습니다.";

    private UserTypeException(
        final String detailMessage,
        final CodeAndMessage codeAndMessage
    ) {
        super(detailMessage, codeAndMessage);
    }

    public static UserTypeException from(final UserCodeAndMessage codeAndMessage) {
        return switch (codeAndMessage) {
            case USER_NOT_FOUND -> new UserNotFoundException(USER_NOT_EXIST_ERROR_MESSAGE, codeAndMessage);
            case ACTIVE_USER_NOT_FOUND -> new UserNotFoundException(ACTIVE_USER_NOT_FOUND, codeAndMessage);
            case USER_EMAIL_CONFLICT -> new UserEmailConflictException(codeAndMessage);
            default -> new UserUnResolvedException(codeAndMessage);
        };
    }

    private static final class UserNotFoundException extends UserTypeException {
        public UserNotFoundException(final String detailMessage, final UserCodeAndMessage codeAndMessage) {
            super(detailMessage, codeAndMessage);
        }
    }

    private static final class UserEmailConflictException extends UserTypeException {
        public UserEmailConflictException(final UserCodeAndMessage codeAndMessage) {
            super(CONFLICT_EMAIL_ERROR_MESSAGE, codeAndMessage);
        }
    }

    private static final class UserUnResolvedException extends UserTypeException {
        public UserUnResolvedException(final UserCodeAndMessage codeAndMessage) {
            super(UN_RESOLVED_MESSAGE, codeAndMessage);
        }
    }
}
