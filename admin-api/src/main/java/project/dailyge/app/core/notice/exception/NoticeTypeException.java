package project.dailyge.app.core.notice.exception;

import project.dailyge.app.codeandmessage.CodeAndMessage;
import project.dailyge.app.common.exception.BusinessException;

import java.util.HashMap;
import java.util.Map;

public sealed class NoticeTypeException extends BusinessException {

    private static final Map<NoticeCodeAndMessage, NoticeTypeException> exceptionMap = new HashMap<>();

    private NoticeTypeException(final CodeAndMessage codeAndMessage) {
        super(codeAndMessage);
    }

    static {
        exceptionMap.put(NoticeCodeAndMessage.NOTICE_NOT_FOUND, new NoticeNotFoundException(NoticeCodeAndMessage.NOTICE_NOT_FOUND));
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
