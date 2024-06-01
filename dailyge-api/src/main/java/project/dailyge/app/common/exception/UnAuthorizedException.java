package project.dailyge.app.common.exception;

import lombok.Getter;
import project.dailyge.app.common.codeandmessage.CodeAndMessage;
import project.dailyge.app.common.codeandmessage.CommonCodeAndMessage;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;

@Getter
public class UnAuthorizedException extends CommonException {

    public UnAuthorizedException(
        final String detailMessage,
        final CodeAndMessage codeAndMessage
    ) {
        super(detailMessage, codeAndMessage);
    }

    public UnAuthorizedException(
    ) {
        super(null, UN_AUTHORIZED);
    }
}
