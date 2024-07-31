package project.dailyge.app.common.exception;

import lombok.Getter;
import project.dailyge.app.codeandmessage.CodeAndMessage;

@Getter
public class DaoException extends CommonException {

    public DaoException(
        final String detailMessage,
        final CodeAndMessage codeAndMessage
    ) {
        super(detailMessage, codeAndMessage);
    }
}
