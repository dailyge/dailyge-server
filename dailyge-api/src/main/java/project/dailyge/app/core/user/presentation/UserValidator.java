package project.dailyge.app.core.user.presentation;

import org.springframework.stereotype.Component;
import project.dailyge.app.common.exception.UnAuthorizedException;

import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;

@Component
public class UserValidator {

    private static final String USER_MISMATCH_MESSAGE = "사용자 정보가 일치하지 않습니다.";

    public void validate(
        final Long loginUserId,
        final Long userId
    ) {
        if (!loginUserId.equals(userId)) {
            throw new UnAuthorizedException(USER_MISMATCH_MESSAGE, UN_AUTHORIZED);
        }
    }
}
