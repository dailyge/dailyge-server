package project.dailyge.app.test.task.integrationtest;


import static java.time.LocalDate.now;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.command.TaskCreateCommand;
import project.dailyge.app.core.task.facade.TaskFacade;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.fixture.user.UserFixture;
import project.dailyge.document.task.MonthlyTaskDocument;
import project.dailyge.document.task.TaskDocumentReadRepository;
import project.dailyge.entity.user.UserJpaEntity;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("[IntegrationTest] 할 일 저장 통합 테스트")
public class TaskSaveIntegrationTest extends DatabaseTestBase {

    @Autowired
    private TaskFacade taskFacade;

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Autowired
    private TaskDocumentReadRepository monthlyTaskReadRepository;

    @Test
    @DisplayName("할 일이 저장되면, Id가 Null이 아니다.")
    void whenSaveTaskThenTaskIdShouldNotBeNull() {
        final UserJpaEntity newUser = userWriteUseCase.save(UserFixture.createUserJpaEntity());
        final DailygeUser dailygeUser = new DailygeUser(newUser);
        final LocalDate now = LocalDate.now();
        taskFacade.createMonthlyTasks(dailygeUser, now);
        final MonthlyTaskDocument findMonthlyTask = monthlyTaskReadRepository.findMonthlyDocumentByUserId(dailygeUser.getUserId(), now).get();
        final TaskCreateCommand command = new TaskCreateCommand(
            findMonthlyTask.getId(),
            "독서",
            "Kafka 완벽가이드 1~30p 읽기",
            now()
        );

        final String newTaskId = taskFacade.save(dailygeUser, command);
        assertNotNull(newTaskId);
    }

    @Test
    @Order(1)
    @DisplayName("연간 일정표를 생성하면 12개(January-December)의 월간 일정표가 생성된다.")
    void whenCreateMonthlyTasksThenResultShould12() {
        final UserJpaEntity newUser = userWriteUseCase.save(UserFixture.createUserJpaEntity());
        final DailygeUser dailygeUser = new DailygeUser(newUser);
        final LocalDate date = LocalDate.now();
        taskFacade.createMonthlyTasks(dailygeUser, date);

        final long count = monthlyTaskReadRepository.countMonthlyTask(dailygeUser.getUserId(), date);
        assertEquals(12, count);
    }

    @Test
    @Order(2)
    @DisplayName("멀티 쓰레드 환경에서도, Redisson 분산락으로 인해 동시성이 보장된다.")
    void whenMultiThreadTryToCreateMonthlyTasksThenResultShouldBeSafe() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(100);
        final UserJpaEntity newUser = userWriteUseCase.save(UserFixture.createUserJpaEntity());
        final DailygeUser dailygeUser = new DailygeUser(newUser);
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
