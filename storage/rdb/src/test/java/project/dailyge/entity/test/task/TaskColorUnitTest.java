package project.dailyge.entity.test.task;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import project.dailyge.entity.task.TaskColor;

@DisplayName("[UnitTest] TaskColor 단위 테스트")
class TaskColorUnitTest {

    @ParameterizedTest
    @ValueSource(strings = {"BLUE", "DARK", "GRAY", "GREEN", "NAVY", "ORANGE", "PINK", "PURPLE", "RED", "YELLOW"})
    @DisplayName("valueOf로 색상을 조죄할 수 있다.")
    void whenUesValueOfThenResultShouldBeNotNll(final String color) {
        assertNotNull(TaskColor.valueOf(color));
    }
}
