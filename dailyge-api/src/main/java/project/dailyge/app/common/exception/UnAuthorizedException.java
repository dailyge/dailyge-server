package project.dailyge.app.common.exception;

import lombok.Getter;
import project.dailyge.app.common.codeandmessage.CodeAndMessage;

@Getter
public class UnAuthorizedException extends CommonException {

    public static final String USER_NOT_FOUND_MESSAGE = "존재하지 않는 유저 정보입니다.";

    public UnAuthorizedException(
        final String detailMessage,
        final CodeAndMessage codeAndMessage
    ) {
        super(detailMessage, codeAndMessage);
    }
}
