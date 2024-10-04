package project.dailyge.entity.test.emoji;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.emoji.EmojiJpaEntity;

@DisplayName("[UnitTest] 이모티콘 엔티티 테스트")
class EmojiJpaEntityUnitTest {

    private EmojiJpaEntity emoji;

    @BeforeEach
    void setUp() {
        emoji = new EmojiJpaEntity("😀");
    }

    @Test
    @DisplayName("올바른 이모티콘을 입력하면 객체가 생성된다.")
    void whenValidEmojiThenObjectIsCreated() {
        final EmojiJpaEntity validEmoji = new EmojiJpaEntity("😀");
        assertAll(
            () -> assertThat(validEmoji.getEmoji()).isEqualTo("😀"),
            () -> assertThat(validEmoji.getId()).isNull()
        );
    }

    @Test
    @DisplayName("빈 문자열을 입력하면 IllegalArgumentException이 발생한다.")
    void whenEmojiIsBlankThenThrowIllegalArgumentException() {
        assertThatThrownBy(() ->
            new EmojiJpaEntity(" ")
        ).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("올바른 이모티콘을 입력해주세요.");
    }
}
