package project.dailyge.app.test.task.integrationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskRecurrenceReadService;
import project.dailyge.app.core.task.application.TaskRecurrenceWriteService;
import project.dailyge.app.core.task.application.TaskWriteService;
import project.dailyge.app.core.task.application.command.TaskRecurrenceCreateCommand;
import project.dailyge.app.paging.Cursor;
import project.dailyge.entity.task.RecurrenceType;
import project.dailyge.entity.task.TaskColor;
import project.dailyge.entity.task.TaskRecurrenceJpaEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static project.dailyge.app.paging.Cursor.createCursor;
import static project.dailyge.app.test.task.fixture.TaskRecurrenceCommandFixture.createTaskRecurrenceCreateCommand;
import static project.dailyge.entity.user.Role.NORMAL;

@DisplayName("[IntegrationTest] 반복 일정 조회 통합 테스트")
class TaskRecurrenceReadIntegrationTest extends DatabaseTestBase {

    @Autowired
    private TaskRecurrenceReadService taskRecurrenceReadService;

    @Autowired
    private TaskRecurrenceWriteService taskRecurrenceWriteService;

    @Autowired
    private TaskWriteService taskWriteService;

    private DailygeUser anotherUser;

    @BeforeEach
    void setUp() {
        anotherUser = new DailygeUser(10000L, NORMAL);
        taskWriteService.saveAll(dailygeUser, LocalDate.now());
        taskWriteService.saveAll(anotherUser, LocalDate.now());
        saveBulks(dailygeUser, 1);
        saveBulks(anotherUser, 1);
    }

    @Test
    @DisplayName("index가 존재하지 않으면, 가장 오래된 데이터를 반환한다.")
    void whenIndexNotExistsThenTheOldestDataShouldBeReturned() {
        final Cursor cursor = createCursor(null, 10);
        final List<TaskRecurrenceJpaEntity> taskRecurrences = taskRecurrenceReadService.findTaskRecurrencesByCursor(dailygeUser, cursor);
        assertEquals(1, taskRecurrences.size());
    }

    @Test
    @DisplayName("index가 존재하면, 그 뒤의 데이터를 읽어온다.")
    void whenIndexExistsThenAfterIndexDataShouldBeReturned() {
        final TaskRecurrenceCreateCommand taskRecurrenceCreateCommand = createTaskRecurrenceCreateCommand(LocalDate.now(), LocalDate.now().plusDays(7), dailygeUser);
        final Long taskRecurrenceId = taskRecurrenceWriteService.save(dailygeUser, taskRecurrenceCreateCommand);
        final Cursor cursor = createCursor(taskRecurrenceId, 5);
        saveBulks(dailygeUser, 1);
        saveBulks(anotherUser, 1);
        final List<TaskRecurrenceJpaEntity> taskRecurrences = taskRecurrenceReadService.findTaskRecurrencesByCursor(dailygeUser, cursor);
        assertEquals(1, taskRecurrences.size());
    }

    private void saveBulks(
        final DailygeUser dailygeUser,
        final int count
    ) {
        for (int idx = 0; idx < count; idx++) {
            final TaskRecurrenceCreateCommand taskRecurrenceCreateCommand = new TaskRecurrenceCreateCommand(
                "test" + idx,
                "description" + idx,
                TaskColor.GRAY,
                RecurrenceType.DAILY,
                null,
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                dailygeUser.getUserId()
            );
            taskRecurrenceWriteService.save(dailygeUser, taskRecurrenceCreateCommand);
        }
    }
}
