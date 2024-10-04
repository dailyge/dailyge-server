package project.dailyge.app.test.emoji.integrationtest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.emoji.application.EmojiReadService;
import project.dailyge.app.core.emoji.exception.EmojiCodeAndMessage;
import project.dailyge.app.core.emoji.exception.EmojiTypeException;

@DisplayName("[IntegrationTest] Emoji 저장 통합 테스트")
class EmojiReadIntegrationTest extends DatabaseTestBase {

    @Autowired
    private EmojiReadService emojiReadService;

    @Test
    @DisplayName("이모티콘이 존재하지 않으면 EmojiTypeException이 발생한다.")
    void whenEmojiNotExistsThenEmojiTypeExceptionShouldBeHappen() {
        final Long invalidEmojiId = Long.MAX_VALUE;
        assertThatThrownBy(() -> emojiReadService.validateExists(invalidEmojiId))
            .isInstanceOf(EmojiTypeException.class)
            .hasMessage(EmojiCodeAndMessage.EMOJI_NOT_FOUND.message());
    }
}
