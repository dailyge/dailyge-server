package project.dailyge.app.common.exception;

import project.dailyge.app.common.codeandmessage.CodeAndMessage;

public class ExternalServerException extends CommonException {

    public ExternalServerException(
        final String detailMessage,
        final CodeAndMessage codeAndMessage
    ) {
        super(detailMessage, codeAndMessage);
    }
}
