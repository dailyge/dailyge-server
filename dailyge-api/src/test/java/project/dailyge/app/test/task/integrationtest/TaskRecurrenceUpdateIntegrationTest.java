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
import project.dailyge.app.core.task.application.command.TaskRecurrenceUpdateCommand;
import project.dailyge.app.core.task.exception.TaskTypeException;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.task.TaskRecurrenceEntityReadRepository;
import project.dailyge.entity.task.TaskRecurrenceJpaEntity;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_RECURRENCE_NOT_FOUND;
import static project.dailyge.app.test.task.fixture.TaskRecurrenceCommandFixture.createTaskRecurrenceCreateCommand;
import static project.dailyge.entity.task.TaskColor.RED;

@DisplayName("[IntegrationTest] 반복 일정 업데이트 통합 테스트")
class TaskRecurrenceUpdateIntegrationTest extends DatabaseTestBase {

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nowDate;
    private TaskRecurrenceCreateCommand createCommand;
    private TaskRecurrenceUpdateCommand updateCommand;

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
        updateCommand = getUpdateCommand();
    }

    @Test
    @DisplayName("반복 일정을 업데이트하면 반복일정과 이후 태스크가 수정된다.")
    void whenUpdateTaskRecurrenceThenShouldUpdateTaskRecurrenceAndFutureTasks() {
        final Long taskRecurrenceId = taskRecurrenceWriteService.save(dailygeUser, createCommand);
        taskRecurrenceWriteService.update(dailygeUser, taskRecurrenceId, updateCommand);
        final TaskRecurrenceJpaEntity taskRecurrence = taskRecurrenceEntityReadRepository.findById(taskRecurrenceId).orElseThrow(IllegalArgumentException::new);
        final List<TaskJpaEntity> afterTasks = taskRecurrenceEntityReadRepository.findTasksAfterStartDateById(taskRecurrenceId, nowDate);
        final List<TaskJpaEntity> beforeTasks = taskRecurrenceEntityReadRepository.findTaskBeforeStartDateById(taskRecurrenceId, nowDate);
        assertAll(
            () -> assertEquals(updateCommand.title(), taskRecurrence.getTitle()),
            () -> assertEquals(updateCommand.content(), taskRecurrence.getContent())
        );
        for (final TaskJpaEntity task : afterTasks) {
            assertAll(
                () -> assertEquals(updateCommand.title(), task.getTitle()),
                () -> assertEquals(updateCommand.content(), task.getContent()),
                () -> assertEquals(updateCommand.color(), task.getColor())
            );
        }
        for (final TaskJpaEntity task : beforeTasks) {
            assertAll(
                () -> assertEquals(createCommand.title(), task.getTitle()),
                () -> assertEquals(createCommand.content(), task.getContent()),
                () -> assertEquals(createCommand.color(), task.getColor())
            );
        }
    }

    @Test
    @DisplayName("권한이 없는 사용자가 반복일정을 수정하면 예외를 던진다.")
    void whenUpdateByInvalidUserThenThrowCommonException() {
        final Long taskRecurrenceId = taskRecurrenceWriteService.save(dailygeUser, createCommand);
        assertThatThrownBy(() ->
            taskRecurrenceWriteService.update(invalidUser, taskRecurrenceId, updateCommand))
            .isInstanceOf(CommonException.class)
            .hasMessage(UN_AUTHORIZED.message());
    }

    @Test
    @DisplayName("유효하지 않은 반복 일정 ID를 업데이트 하면 예외를 던진다.")
    void whenUpdateInvalidTaskRecurrenceThenThrowTaskTypeException() {
        final Long invalidId = Long.MAX_VALUE;
        assertThatThrownBy(() ->
            taskRecurrenceWriteService.update(dailygeUser, invalidId, updateCommand))
            .isInstanceOf(TaskTypeException.class)
            .hasMessage(TASK_RECURRENCE_NOT_FOUND.message());
    }

    private TaskRecurrenceUpdateCommand getUpdateCommand() {
        return new TaskRecurrenceUpdateCommand(
            "배형",
            "오전 8시 반",
            RED
        );
    }
}
