package project.dailyge.app.common.exception;

import lombok.Getter;
import project.dailyge.app.common.codeandmessage.CodeAndMessage;

@Getter
public class CommonException extends RuntimeException {

    private String detailMessage;
    private final CodeAndMessage codeAndMessage;

    public CommonException(final CodeAndMessage codeAndMessage) {
        super(codeAndMessage.message());
        this.codeAndMessage = codeAndMessage;
    }

    public CommonException(
        final String detailMessage,
        final CodeAndMessage codeAndMessage
    ) {
        super(detailMessage);
        this.detailMessage = detailMessage;
        this.codeAndMessage = codeAndMessage;
    }
}
