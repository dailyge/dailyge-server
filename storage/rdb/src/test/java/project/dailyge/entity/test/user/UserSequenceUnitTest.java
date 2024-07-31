package project.dailyge.entity.test.user;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.user.UserSequence;

@DisplayName("[UnitTest] UserSequence 단위 테스트")
class UserSequenceTest {

    @Test
    @DisplayName("UserSequence 생성 시, 기본값이 설정된 UserSequence 인스턴스가 생성된다.")
    void whenUserSequenceCreateThenUserSequenceShouldHaveDefaultValues() {
        final UserSequence userSequence = UserSequence.createNewSequence();

        assertAll(
            () -> assertNotNull(userSequence),
            () -> assertNull(userSequence.getId()),
            () -> assertFalse(userSequence.isExecuted())
        );
    }

    @Test
    @DisplayName("UserSequence 인스턴스 생성 시, 생성자를 통한 값 설정이 정상적으로 이루어진다.")
    void whenUserSequenceCreateWithConstructorThenValuesShouldBeSetCorrectly() {
        final Long id = 1L;
        final Boolean executed = true;
        final UserSequence userSequence = new UserSequence(id, executed);

        assertAll(
            () -> assertEquals(id, userSequence.getId()),
            () -> assertEquals(executed, userSequence.isExecuted())
        );
    }

    @Test
    @DisplayName("UserSequence 상태를 변경하면, 변경된 상태가 반영된다.")
    void whenStatusIsFalseThenAndChangeThenResultShouldBeFalse() {
        final UserSequence userSequence = UserSequence.createNewSequence();
        userSequence.changeExecuted();

        assertTrue(userSequence.isExecuted());
    }

    @Test
    @DisplayName("UserSequence 상태가 True이면, 상태가 변경되지 않는다.")
    void whenStatusIsTrueThenAndChangeThenResultShouldBeTrue() {
        final UserSequence userSequence = new UserSequence(1L, true);
        userSequence.changeExecuted();

        assertTrue(userSequence.isExecuted());
    }
}
