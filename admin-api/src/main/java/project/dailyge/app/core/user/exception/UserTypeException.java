package project.dailyge.app.core.user.exception;

import project.dailyge.app.codeandmessage.CodeAndMessage;
import project.dailyge.app.common.exception.BusinessException;

import java.util.HashMap;
import java.util.Map;

public sealed class UserTypeException extends BusinessException {

    private static final Map<UserCodeAndMessage, UserTypeException> exceptionMap = new HashMap<>();

    private UserTypeException(final CodeAndMessage codeAndMessage) {
        super(codeAndMessage);
    }

    static {
        exceptionMap.put(UserCodeAndMessage.USER_NOT_FOUND, new UserNotFoundException(UserCodeAndMessage.USER_NOT_FOUND));
    }

    public static UserTypeException from(final UserCodeAndMessage codeAndMessage) {
        final UserTypeException userTypeException = exceptionMap.get(codeAndMessage);
        if (userTypeException == null) {
            return new UserUnResolvedException(codeAndMessage);
        }
        return userTypeException;
    }

    private static final class UserNotFoundException extends UserTypeException {
        public UserNotFoundException(final UserCodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class UserUnResolvedException extends UserTypeException {
        public UserUnResolvedException(final UserCodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }
}
