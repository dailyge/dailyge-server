package project.dailyge.app.common.exception;

import project.dailyge.app.codeandmessage.CodeAndMessage;

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
        super(codeAndMessage.message());
        this.detailMessage = detailMessage;
        this.codeAndMessage = codeAndMessage;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public CodeAndMessage getCodeAndMessage() {
        return codeAndMessage;
    }

    protected void addDetailMessage(final String detailMessage) {
        this.detailMessage = detailMessage;
    }

    public int getCode() {
        return codeAndMessage.code();
    }
}
