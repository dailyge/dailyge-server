package project.dailyge.app.core.emoji.exception;

import lombok.Getter;
import project.dailyge.app.codeandmessage.CodeAndMessage;
import project.dailyge.app.common.exception.BusinessException;
import static project.dailyge.app.core.emoji.exception.EmojiCodeAndMessage.EMOJI_NOT_FOUND;
import static project.dailyge.app.core.emoji.exception.EmojiCodeAndMessage.EMOJI_UN_RESOLVED_EXCEPTION;

import java.util.HashMap;
import java.util.Map;

@Getter
public sealed class EmojiTypeException extends BusinessException {

    private static final Map<CodeAndMessage, EmojiTypeException> factory = new HashMap<>();

    private EmojiTypeException(final CodeAndMessage codeAndMessage) {
        super(codeAndMessage);
    }

    static {
        factory.put(EMOJI_NOT_FOUND, new EmojiNotFoundException(EMOJI_NOT_FOUND));
        factory.put(EMOJI_UN_RESOLVED_EXCEPTION, new EmojiUnResolvedException(EMOJI_UN_RESOLVED_EXCEPTION));
    }

    public static EmojiTypeException from(final EmojiCodeAndMessage codeAndMessage) {
        return getException(codeAndMessage);
    }

    public static EmojiTypeException from(
        final String detailMessage,
        final EmojiCodeAndMessage codeAndMessage
    ) {
        final EmojiTypeException emojiTypeException = getException(codeAndMessage);
        if (detailMessage != null) {
            emojiTypeException.addDetailMessage(detailMessage);
        }
        return emojiTypeException;
    }

    private static EmojiTypeException getException(final CodeAndMessage codeAndMessage) {
        final EmojiTypeException findEmojiTypeException = factory.get(codeAndMessage);
        if (findEmojiTypeException != null) {
            return findEmojiTypeException;
        }
        return factory.get(EMOJI_UN_RESOLVED_EXCEPTION);
    }

    @Override
    protected void addDetailMessage(final String detailMessage) {
        super.addDetailMessage(detailMessage);
    }

    private static final class EmojiNotFoundException extends EmojiTypeException {
        private EmojiNotFoundException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class EmojiUnResolvedException extends EmojiTypeException {
        private EmojiUnResolvedException(final CodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }
}
