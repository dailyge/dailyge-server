package project.dailyge.entity.test.task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.task.TaskLabelJpaEntity;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("[UnitTest] TaskLabel 단위 테스트")
class TaskLabelJpaEntityUnitTest {

    @Test
    @DisplayName("올바른 인자가 들어오면 TaskLabelJpaEntity가 생성된다.")
    void whenValidArgumentsThenTaskTaskLabelJpaEntityShouldBeCreated() {
        final TaskLabelJpaEntity newTaskLabel = new TaskLabelJpaEntity(
            "개발",
            "개발 관련 일정",
            "01153E",
            1L
        );
        assertAll(
            () -> assertEquals("개발", newTaskLabel.getName()),
            () -> assertEquals("개발 관련 일정", newTaskLabel.getDescription()),
            () -> assertEquals("01153E", newTaskLabel.getColor()),
            () -> assertEquals(1L, newTaskLabel.getUserId())
        );
    }
}
