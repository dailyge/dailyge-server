package project.dailyge.app.common.exception;

import lombok.Getter;
import project.dailyge.app.codeandmessage.CodeAndMessage;

@Getter
public class InvalidParameterException extends CommonException {

    private final String detailMessage;
    private final CodeAndMessage codeAndMessage;

    public InvalidParameterException(
        final String detailMessage,
        final CodeAndMessage codeAndMessage
    ) {
        super(detailMessage, codeAndMessage);
        this.detailMessage = detailMessage;
        this.codeAndMessage = codeAndMessage;
    }

    public InvalidParameterException(final CodeAndMessage codeAndMessage) {
        super(codeAndMessage);
        this.detailMessage = null;
        this.codeAndMessage = codeAndMessage;
    }
}
