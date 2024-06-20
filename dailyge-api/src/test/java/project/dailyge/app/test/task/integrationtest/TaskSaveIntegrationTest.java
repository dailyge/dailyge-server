package project.dailyge.app.test.task.integrationtest;


import static java.time.LocalDate.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import project.dailyge.app.common.*;
import project.dailyge.app.core.task.facade.*;
import project.dailyge.app.core.user.application.*;
import project.dailyge.app.fixture.user.UserFixture;
import project.dailyge.entity.task.*;
import static project.dailyge.entity.task.TaskStatus.*;
import project.dailyge.entity.user.*;

@DisplayName("[IntegrationTest] 할 일 저장 통합 테스트")
public class TaskSaveIntegrationTest extends DatabaseTestBase {

    @Autowired
    private TaskFacade taskFacade;

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Test
    @DisplayName("할 일이 저장되면, Id가 Null이 아니다.")
    void whenSaveTaskThenTaskIdShouldNotBeNull() {
        final UserJpaEntity newUser = userWriteUseCase.save(UserFixture.createUserJpaEntity());
        final TaskJpaEntity newTask = new TaskJpaEntity("독서", "Kafka 완벽가이드 1~30p 읽기", now(), TODO, newUser.getId());

        final TaskJpaEntity savedTask = taskFacade.save(newTask);

        assertNotNull(savedTask.getId());
    }
}
