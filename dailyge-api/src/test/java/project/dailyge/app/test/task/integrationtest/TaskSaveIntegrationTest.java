package project.dailyge.app.test.task.integrationtest;


import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.task.application.TaskWriteUseCase;
import project.dailyge.app.core.task.application.command.TaskCreateCommand;
import project.dailyge.app.core.task.facade.TaskFacade;
import static project.dailyge.app.test.task.fixture.TaskCommandFixture.createTaskCreationCommand;
import project.dailyge.entity.task.MonthlyTaskEntityReadRepository;
import project.dailyge.entity.task.MonthlyTaskJpaEntity;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("[IntegrationTest] 할 일 저장 통합 테스트")
class TaskSaveIntegrationTest extends DatabaseTestBase {

    @Autowired
    private TaskFacade taskFacade;

    @Autowired
    private TaskWriteUseCase taskWriteUseCase;

    @Autowired
    private MonthlyTaskEntityReadRepository monthlyTaskReadRepository;

    @Test
    @Order(1)
    @DisplayName("할 일이 저장되면, Id가 Null이 아니다.")
    void whenSaveTaskThenTaskIdShouldNotBeNull() {
        final LocalDate now = LocalDate.now();
        taskFacade.createMonthlyTasks(dailygeUser, now);
        final TaskCreateCommand command = createTaskCreationCommand(now);

        final Long newTaskId = taskWriteUseCase.save(dailygeUser, command);
        assertNotNull(newTaskId);
    }

    @Test
    @Order(2)
    @DisplayName("연간 일정표를 생성하면 12개(January-December)의 월간 일정표가 생성된다.")
    void whenCreateMonthlyTasksThenResultShould12() {
        final LocalDate date = LocalDate.now();
        taskFacade.createMonthlyTasks(dailygeUser, date);

        final long count = monthlyTaskReadRepository.countMonthlyTask(dailygeUser.getUserId(), date);
        assertEquals(12, count);
    }

    @Test
    @Order(3)
    @DisplayName("멀티 쓰레드 환경에서도, Redisson 분산락으로 인해 MonthlyTask동시성이 보장된다.")
    void whenMultiThreadTryToCreateMonthlyTasksThenResultShouldBeSafe() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(100);
        final LocalDate date = LocalDate.now();
        taskFacade.createMonthlyTasks(dailygeUser, date);

        final ExecutorService service = Executors.newFixedThreadPool(32);
        for (int index = 1; index <= 100; index++) {
            try {
                service.execute(() -> {
                    taskFacade.createMonthlyTasks(dailygeUser, date);
                });
            } finally {
                countDownLatch.countDown();
            }
        }

        countDownLatch.await(10, SECONDS);
        final long count = monthlyTaskReadRepository.countMonthlyTask(dailygeUser.getUserId(), date);
        assertEquals(12, count);
    }
}
