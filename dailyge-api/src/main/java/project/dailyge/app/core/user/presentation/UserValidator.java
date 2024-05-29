package project.dailyge.app.core.user.presentation;

import org.springframework.stereotype.Component;
import project.dailyge.app.common.exception.UnAuthorizedException;

import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;

@Component
public class UserValidator {

    private static final String NO_PERMISSIONS_OTHER_USER_MESSAGE = "접근할 수 없는 사용자 정보입니다.";

    public void validate(
        final Long loginUserId,
        final Long userId
    ) {
        if (!loginUserId.equals(userId)) {
            throw new UnAuthorizedException(NO_PERMISSIONS_OTHER_USER_MESSAGE, UN_AUTHORIZED);
        }
    }
}
