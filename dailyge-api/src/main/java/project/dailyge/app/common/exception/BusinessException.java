package project.dailyge.app.common.exception;

import project.dailyge.app.common.codeandmessage.CodeAndMessage;

public class BusinessException extends RuntimeException {

    private String detailMessage;
    private final CodeAndMessage codeAndMessage;

    public BusinessException(final CodeAndMessage codeAndMessage) {
        super(codeAndMessage.message());
        this.codeAndMessage = codeAndMessage;
    }

    public BusinessException(
        final String detailMessage,
        final CodeAndMessage codeAndMessage
    ) {
        super(detailMessage);
        this.detailMessage = detailMessage;
        this.codeAndMessage = codeAndMessage;
    }

}
