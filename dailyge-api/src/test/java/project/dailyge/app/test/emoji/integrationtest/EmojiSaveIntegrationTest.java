package project.dailyge.app.test.emoji.integrationtest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.emoji.application.EmojiReadService;
import project.dailyge.app.core.emoji.application.EmojiWriteService;
import project.dailyge.entity.emoji.EmojiJpaEntity;

@DisplayName("[IntegrationTest] Emoji 저장 통합 테스트")
class EmojiSaveIntegrationTest extends DatabaseTestBase {

    @Autowired
    private EmojiWriteService emojiWriteService;

    @Autowired
    private EmojiReadService emojiReadService;

    @Test
    @DisplayName("이모티콘이 존재하면 예외가 발생하지 않는다.")
    void whenSaveEmojiThenCanFindById() {
        final EmojiJpaEntity emoji = new EmojiJpaEntity(":+1::skin-tone-6:");
        final Long newEmojiId = emojiWriteService.save(emoji);
        assertDoesNotThrow(() -> emojiReadService.validateExists(newEmojiId));
    }
}
