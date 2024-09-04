package project.dailyge.document.task;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[UnitTest] TaskCount 단위 테스트")
class TaskCountUnitTest {

    @Test
    @DisplayName("생성자를 통해 TaskCount 객체를 생성할 수 있다.")
    void whenCreateTaskCountWithConstructorThenObjectShouldBeNotNull() {
        final long expectedCount = 5L;
        final TaskCount taskCount = new TaskCount(expectedCount);

        assertAll(() -> assertNotNull(taskCount), () -> assertEquals(expectedCount, taskCount.getCount()));
    }
}
