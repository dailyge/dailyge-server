package project.dailyge.entity.test.anniversary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.anniversary.AnniversaryJpaEntity;

import java.time.LocalDateTime;

@DisplayName("[UnitTest] Anniversary 엔티티 단위 테스트")
class AnniversaryJpaEntityUnitTest {

    private AnniversaryJpaEntity anniversary;

    @BeforeEach
    void setUp() {
        anniversary = new AnniversaryJpaEntity(
            "생일",
            LocalDateTime.of(2024, 10, 10, 0, 0),
            true,
            1L,
            1L
        );
    }

    @Test
    @DisplayName("올바른 파라미터가 들어왔다면 객체가 생성된다.")
    void whenValidParameterThenAnniversaryShouldBeCreated() {
        assertAll(
            () -> assertThat(anniversary.getName()).isEqualTo("생일"),
            () -> assertThat(anniversary.getDate()).isEqualTo(LocalDateTime.of(2024, 10, 10, 0, 0)),
            () -> assertThat(anniversary.isRemind()).isTrue(),
            () -> assertThat(anniversary.getEmojiId()).isEqualTo(1L),
            () -> assertThat(anniversary.getUserId()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("기념일을 삭제하면 deleted 필드가 True가 된다.")
    void whenDeletedThenDeleteStatusShouldBeTrue() {
        anniversary.delete();
        assertTrue(anniversary.getDeleted());
    }
}
