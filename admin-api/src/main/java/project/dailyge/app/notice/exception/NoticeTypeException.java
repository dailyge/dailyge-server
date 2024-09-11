package project.dailyge.app.notice.exception;

import java.util.HashMap;
import java.util.Map;
import project.dailyge.app.codeandmessage.CodeAndMessage;
import project.dailyge.app.common.exception.BusinessException;
import static project.dailyge.app.notice.exception.NoticeCodeAndMessage.NOTICE_NOT_FOUND;

public sealed class NoticeTypeException extends BusinessException {

    private static final Map<NoticeCodeAndMessage, NoticeTypeException> exceptionMap = new HashMap<>();

    private NoticeTypeException(final CodeAndMessage codeAndMessage) {
        super(codeAndMessage);
    }

    static {
        exceptionMap.put(NOTICE_NOT_FOUND, new NoticeNotFoundException(NOTICE_NOT_FOUND));
    }

    public static NoticeTypeException from(final NoticeCodeAndMessage codeAndMessage) {
        final NoticeTypeException noticeTypeException = exceptionMap.get(codeAndMessage);
        if (noticeTypeException == null) {
            return new NoticeUnResolvedException(codeAndMessage);
        }
        return noticeTypeException;
    }

    private static final class NoticeNotFoundException extends NoticeTypeException {

        public NoticeNotFoundException(final NoticeCodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class NoticeUnResolvedException extends NoticeTypeException {

        public NoticeUnResolvedException(final NoticeCodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }
}
