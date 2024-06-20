package project.dailyge.app.test.task.documentationtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.task.facade.TaskFacade;
import project.dailyge.app.core.task.presentation.requesst.TaskRegisterRequest;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.user.UserJpaEntity;

import static io.restassured.RestAssured.given;
import static java.time.LocalDate.now;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static project.dailyge.app.fixture.user.UserFixture.createUserJpaEntity;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_AUTHORIZATION_HEADER;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_DELETE_PATH_PARAMETER_SNIPPET;

@DisplayName("[DocumentationTest] 할 일 삭제 문서화 테스트")
class TaskDeleteDocumentationTest extends DatabaseTestBase {

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Autowired
    private TaskFacade taskFacade;

    @Test
    @DisplayName("Task를 삭제하면 204 No-Content 응답을 받는다.")
    void whenDeleteTaskThenStatusCodeShouldBe204_NO_CONTENT() {
        final UserJpaEntity newUser = userWriteUseCase.save(createUserJpaEntity());
        final TaskRegisterRequest request = new TaskRegisterRequest("주간 미팅", "Backend 팀과 Api 스펙 정의", now());
        final DailygeUser dailygeUser = new DailygeUser(newUser);
        final TaskJpaEntity newTaskId = taskFacade.save(request.toEntity(dailygeUser));

        given(this.specification)
            .filter(document(IDENTIFIER,
                TASK_AUTHORIZATION_HEADER,
                TASK_DELETE_PATH_PARAMETER_SNIPPET
                )
            )
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .header(USER_ID_KEY, newUser.getId())
            .when()
            .delete("/api/tasks/{taskId}", newTaskId.getId())
            .then()
            .statusCode(204)
            .log()
            .all();
    }
}
