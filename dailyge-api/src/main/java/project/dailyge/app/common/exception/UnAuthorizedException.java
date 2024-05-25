package project.dailyge.app.common.exception;

import lombok.*;
import project.dailyge.app.common.codeandmessage.*;
import project.dailyge.app.common.exception.*;

@Getter
public class UnAuthorizedException extends CommonException {

    public UnAuthorizedException(
        String detailMessage,
        CodeAndMessage codeAndMessage
    ) {
        super(detailMessage, codeAndMessage);
    }
}
