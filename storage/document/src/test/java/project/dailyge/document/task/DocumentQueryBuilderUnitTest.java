package project.dailyge.document.task;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;
import static project.dailyge.document.task.DocumentQueryBuilder.createMonthlyTaskSearchCriteriaByTaskIds;
import static project.dailyge.document.task.DocumentQueryBuilder.createTaskSearchCriteriaWithUserAndDate;
import static project.dailyge.document.task.DocumentQueryBuilder.createTaskStatusUpdateDefinition;
import static project.dailyge.document.task.DocumentQueryBuilder.createTaskUpdateDefinition;

import java.time.LocalDate;
import java.time.LocalDateTime;

@DisplayName("[UnitTest] DocumentQueryBuilder 단위 테스트")
class DocumentQueryBuilderUnitTest {

    @Test
    @DisplayName("createTaskSearchCriteriaWithUserAndDate 메서드를 통해 Criteria 객체를 생성할 수 있다.")
    void whenCreateTaskSearchCriteriaWithUserAndDateThenCriteriaIsCorrect() {
        final TaskDocument task = mock(TaskDocument.class);
        final LocalDate date = LocalDate.of(2023, 7, 26);

        when(task.getUserId()).thenReturn(123L);

        final Criteria criteria = createTaskSearchCriteriaWithUserAndDate(task, date);
        assertNotNull(criteria);
    }

    @Test
    @DisplayName("createMonthlyTaskSearchCriteriaByTaskIds 메서드를 통해 Criteria 객체를 생성할 수 있다.")
    void whenCreateMonthlyTaskSearchCriteriaByTaskIdsThenCriteriaIsCorrect() {
        final String monthlyTaskId = createTimeBasedUUID();
        final String taskId = "taskId";
        final Criteria criteria = createMonthlyTaskSearchCriteriaByTaskIds(monthlyTaskId, taskId);
        assertNotNull(criteria);
    }

    @Test
    @DisplayName("createTaskDefinition 메서드를 통해 Update 객체를 생성할 수 있다.")
    void whenCreateTaskDefinitionThenUpdateIsCorrect() {
        final TaskDocument task = mock(TaskDocument.class);
        final Update update = DocumentQueryBuilder.createTaskDefinition(task);

        assertNotNull(update);
    }

    @Test
    @DisplayName("createTaskStatusUpdateDefinition 메서드를 통해 Update 객체를 생성할 수 있다.")
    void whenCreateTaskStatusUpdateDefinitionThenUpdateIsCorrect() {
        final Long userId = 123L;
        final String status = "DONE";
        final LocalDateTime now = now();
        final Update update = createTaskStatusUpdateDefinition(userId, status, now);

        assertNotNull(update);
    }

    @Test
    @DisplayName("createTaskUpdateDefinition 메서드를 통해 Update 객체를 생성할 수 있다.")
    void whenCreateTaskUpdateDefinitionThenUpdateIsCorrect() {
        final Long userId = 123L;
        final String title = "New Title";
        final String content = "New Content";
        final LocalDate date = LocalDate.of(2023, 7, 26);
        final String status = "IN_PROGRESS";
        final LocalDateTime now = now();
        final Update update = createTaskUpdateDefinition(userId, title, content, date, status, now);

        assertNotNull(update);
    }
}
