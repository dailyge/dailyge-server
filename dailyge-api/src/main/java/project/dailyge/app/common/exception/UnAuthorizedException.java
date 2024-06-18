package project.dailyge.app.common.exception;

import lombok.Getter;
import project.dailyge.app.common.codeandmessage.CodeAndMessage;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;

@Getter
public class UnAuthorizedException extends CommonException {

    public static final String USER_NOT_FOUND_MESSAGE = "존재하지 않는 사용자 정보입니다.";
    public static final String USER_NOT_MATCH_MESSAGE = "사용자 정보가 일치 하지 않습니다.";
    public static final String INVALID_TOKEN_MESSAGE = "올바르지 않는 사용자 토큰 정보입니다.";

    public UnAuthorizedException(
        final String detailMessage,
        final CodeAndMessage codeAndMessage
    ) {
        super(detailMessage, codeAndMessage);
    }

    public UnAuthorizedException(
    ) {
        super(UN_AUTHORIZED.message(), UN_AUTHORIZED);
    }
}
