package project.dailyge.entity.test.emoji;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.emoji.EmojiJpaEntity;
import project.dailyge.entity.emoji.EmojiType;

@DisplayName("[UnitTest] 이모티콘 엔티티 테스트")
class EmojiJpaEntityUnitTest {

    private EmojiJpaEntity emoji;

    @BeforeEach
    void setUp() {
        emoji = new EmojiJpaEntity(null, "😀", EmojiType.PEOPLE);
    }

    @Test
    @DisplayName("올바른 이모티콘과 유형을 입력하면 객체가 생성된다.")
    void whenValidEmojiAndTypeThenObjectIsCreated() {
        final EmojiJpaEntity validEmoji = new EmojiJpaEntity(null, "😀", EmojiType.PEOPLE);
        assertAll(
            () -> assertThat(validEmoji.getEmoji()).isEqualTo("😀"),
            () -> assertThat(validEmoji.getEmojiType()).isEqualTo(EmojiType.PEOPLE),
            () -> assertThat(validEmoji.getId()).isNull()
        );
    }

    @Test
    @DisplayName("빈 문자열을 입력하면 IllegalArgumentException이 발생한다.")
    void whenEmojiIsBlankThenThrowIllegalArgumentException() {
        assertThatThrownBy(() ->
            new EmojiJpaEntity(null, " ", EmojiType.PEOPLE)
        ).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("올바른 이모티콘을 입력해주세요.");
    }
}
