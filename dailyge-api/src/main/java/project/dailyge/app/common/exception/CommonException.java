package project.dailyge.app.common.exception;

import lombok.*;
import project.dailyge.app.common.codeandmessage.*;

@Getter
public class CommonException extends RuntimeException {

    private final CodeAndMessage codeAndMessage;
    private String detailMessage;

    public CommonException(CodeAndMessage codeAndMessage) {
        super(codeAndMessage.message());
        this.codeAndMessage = codeAndMessage;
    }

    public CommonException(
        String detailMessage,
        CodeAndMessage codeAndMessage
    ) {
        super(detailMessage);
        this.detailMessage = detailMessage;
        this.codeAndMessage = codeAndMessage;
    }
}
