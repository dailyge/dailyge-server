package project.dailyge.app.test.task.integrationtest;


import static java.time.LocalDate.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import project.dailyge.app.common.*;
import project.dailyge.app.core.task.facade.*;
import project.dailyge.app.core.user.application.*;
import project.dailyge.app.fixture.user.UserFixture;
import project.dailyge.domain.task.*;
import static project.dailyge.domain.task.TaskStatus.*;
import project.dailyge.domain.user.*;

@DisplayName("[IntegrationTest] 할 일 저장 통합 테스트")
public class TaskSaveIntegrationTest extends IntegrationTestBase {

    @Autowired
    private TaskFacade taskFacade;

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Test
    @DisplayName("할 일이 저장되면, PK가 Null이 아니다.")
    void taskSaveTest() {
        UserJpaEntity newUser = userWriteUseCase.save(UserFixture.createUserJpaEntity(1L));
        TaskJpaEntity newTask = new TaskJpaEntity("독서", "Kafka 완벽가이드 1~30p 읽기", now(), TODO, newUser.getId());

        TaskJpaEntity savedTask = taskFacade.save(newTask);

        assertNotNull(savedTask.getId());
    }
}
