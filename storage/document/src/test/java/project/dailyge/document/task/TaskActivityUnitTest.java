package project.dailyge.document.task;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;

@DisplayName("[UnitTest] TaskActivity 단위 테스트")
class TaskActivityUnitTest {

    @Test
    @DisplayName("생성자를 통해 TaskActivity를 생성할 수 있다.")
    void whenCreateTaskActivityObjectShouldBeNotNull() {
        assertNotNull(new TaskActivity(1L, createTimeBasedUUID()));
    }

    @Test
    @DisplayName("사용자 ID 비교를 통해 사용자를 구분할 수 있다.")
    void whenCompareToUserIdThenCanKnowTheOwner() {
        final TaskActivity taskActivity = new TaskActivity(1L, createTimeBasedUUID());
        assertTrue(taskActivity.isOwner(1L));
    }
}
