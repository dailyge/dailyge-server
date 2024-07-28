package project.dailyge.app.common.exception;

import project.dailyge.app.codeandmessage.CodeAndMessage;

public class JWTAuthenticationException extends CommonException {

    public static final String INVALID_TOKEN_MESSAGE = "올바르지 않는 사용자 토큰 정보입니다.";
    public static final String TOKEN_SIGNATURE_VERIFICATION_FAILED_ERROR_MESSAGE = "토큰 서명 검증에 실패하였습니다.";
    public static final String TOKEN_FORMAT_INCORRECT_ERROR_MESSAGE = "토큰 형식이 올바르지 않습니다.";
    public static final String EXPIRED_TOKEN_ERROR_MESSAGE = "만료 된 토큰입니다.";
    public static final String UNSUPPORTED_TOKEN_ERROR_MESSAGE = "지원되지 않는 JWT 입니다.";
    public static final String EMPTY_TOKEN_ERROR_MESSAGE = "비어있는 JWT 입니다.";
    public static final String INVALID_ID_TYPE_MESSAGE = "올바르지 않는 사용자 ID 유형입니다.";

    public JWTAuthenticationException(
        final String detailMessage,
        final CodeAndMessage codeAndMessage
    ) {
        super(detailMessage, codeAndMessage);
    }
}
