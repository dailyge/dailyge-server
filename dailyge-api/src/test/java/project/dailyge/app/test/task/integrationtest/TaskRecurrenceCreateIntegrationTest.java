package project.dailyge.app.test.task.integrationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.task.application.TaskRecurrenceWriteService;
import project.dailyge.app.core.task.application.TaskWriteService;
import project.dailyge.app.core.task.application.command.TaskRecurrenceCreateCommand;
import project.dailyge.entity.task.RecurrenceType;
import project.dailyge.entity.task.TaskColor;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("[IntegrationTest] 반복 일정 생성 통합 테스트")
class TaskRecurrenceCreateIntegrationTest extends DatabaseTestBase {

    private LocalDate startDate;
    private LocalDate endDate;
    private TaskRecurrenceCreateCommand taskRecurrenceCreateCommand;

    @Autowired
    private TaskWriteService taskWriteService;

    @Autowired
    private TaskRecurrenceWriteService taskRecurrenceWriteService;

    @BeforeEach
    void setUp() {
        startDate = LocalDate.now();
        endDate = startDate.plusMonths(10);
        taskWriteService.saveAll(dailygeUser, startDate);
    }

    @Test
    @DisplayName("반복 일정이 저장되면, Id가 Null이 아니다.")
    void whenCreateTaskRecurrenceThenIdShouldNotBeNull() {
        taskRecurrenceCreateCommand = new TaskRecurrenceCreateCommand(
            "수영",
            "오전 7시 반",
            TaskColor.GRAY,
            RecurrenceType.WEEKLY,
            List.of(1, 3, 5),
            startDate,
            endDate
        );
        final Long taskRecurrenceId = taskRecurrenceWriteService.save(dailygeUser, taskRecurrenceCreateCommand);
        assertNotNull(taskRecurrenceId);
    }
}
