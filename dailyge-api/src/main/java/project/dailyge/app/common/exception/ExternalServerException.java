package project.dailyge.app.common.exception;

import project.dailyge.app.common.codeandmessage.CodeAndMessage;

public class ExternalServerException extends CommonException {

    public static final String REDIS_SAVE_FAILED_MESSAGE = "레디스 데이터 저장에 실패하였습니다.";
    public static final String REDIS_SEARCH_FAILED_MESSAGE = "레디스 데이터 조회에 실패하였습니다.";

    public ExternalServerException(
        String detailMessage,
        CodeAndMessage codeAndMessage
    ) {
        super(detailMessage, codeAndMessage);
    }
}
