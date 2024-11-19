package project.dailyge.app.test.task.integrationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.task.application.TaskRecurrenceWriteService;
import project.dailyge.app.core.task.application.TaskWriteService;
import project.dailyge.app.core.task.application.command.TaskRecurrenceCreateCommand;
import project.dailyge.app.core.task.exception.TaskTypeException;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.task.TaskRecurrenceEntityReadRepository;
import project.dailyge.entity.task.TaskRecurrenceJpaEntity;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_RECURRENCE_NOT_FOUND;
import static project.dailyge.app.test.task.fixture.TaskRecurrenceCommandFixture.createTaskRecurrenceCreateCommand;

@DisplayName("[IntegrationTest] 반복 일정 삭제 통합 테스트")
class TaskRecurrenceDeleteIntegrationTest extends DatabaseTestBase {

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nowDate;
    private TaskRecurrenceCreateCommand createCommand;

    @Autowired
    private TaskWriteService taskWriteService;

    @Autowired
    private TaskRecurrenceWriteService taskRecurrenceWriteService;

    @Autowired
    private TaskRecurrenceEntityReadRepository taskRecurrenceEntityReadRepository;

    @BeforeEach
    void setUp() {
        nowDate = LocalDate.now();
        startDate = nowDate.minusMonths(3);
        endDate = startDate.plusMonths(6);
        taskWriteService.saveAll(dailygeUser, startDate);
        createCommand = createTaskRecurrenceCreateCommand(startDate, endDate, dailygeUser);
    }

    @Test
    @DisplayName("반복 일정을 삭제하면 반복일정과 이후 태스크가 삭제된다.")
    void whenDeleteTaskRecurrenceThenShouldDeleteTaskRecurrenceAndFutureTasks() {
        final Long taskRecurrenceId = taskRecurrenceWriteService.save(dailygeUser, createCommand);
        taskRecurrenceWriteService.delete(dailygeUser, taskRecurrenceId);
        final TaskRecurrenceJpaEntity taskRecurrence = taskRecurrenceEntityReadRepository.findByIdAndDeleted(taskRecurrenceId).orElseThrow(IllegalArgumentException::new);
        final List<TaskJpaEntity> afterTasks = taskRecurrenceEntityReadRepository.findTasksAfterStartDateById(taskRecurrenceId, nowDate);
        final List<TaskJpaEntity> beforeTasks = taskRecurrenceEntityReadRepository.findTaskBeforeStartDateById(taskRecurrenceId, nowDate);
        assertTrue(taskRecurrence.getDeleted());
        for (final TaskJpaEntity task : afterTasks) {
            assertTrue(task.getDeleted());
        }
        for (final TaskJpaEntity task : beforeTasks) {
            assertFalse(task.getDeleted());
        }
    }

    @Test
    @DisplayName("권한이 없는 사용자가 반복일정을 수정하면 예외를 던진다.")
    void whenDeleteByInvalidUserThenThrowCommonException() {
        final Long taskRecurrenceId = taskRecurrenceWriteService.save(dailygeUser, createCommand);
        assertThatThrownBy(() ->
            taskRecurrenceWriteService.delete(invalidUser, taskRecurrenceId))
            .isInstanceOf(CommonException.class)
            .hasMessage(UN_AUTHORIZED.message());
    }

    @Test
    @DisplayName("유효하지 않은 반복 일정 ID를 업데이트 하면 예외를 던진다.")
    void whenDeleteInvalidTaskRecurrenceThenThrowTaskTypeException() {
        final Long invalidId = Long.MAX_VALUE;
        assertThatThrownBy(() ->
            taskRecurrenceWriteService.delete(dailygeUser, invalidId))
            .isInstanceOf(TaskTypeException.class)
            .hasMessage(TASK_RECURRENCE_NOT_FOUND.message());
    }
}
