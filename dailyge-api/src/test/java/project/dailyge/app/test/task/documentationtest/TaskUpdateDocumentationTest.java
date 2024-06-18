package project.dailyge.app.test.task.documentationtest;

import static io.restassured.RestAssured.given;
import static java.time.LocalDate.now;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.task.facade.TaskFacade;
import project.dailyge.app.core.task.presentation.requesst.TaskRegisterRequest;
import project.dailyge.app.core.task.presentation.requesst.TaskUpdateRequest;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.fixture.user.UserFixture;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_AUTHORIZATION_HEADER;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_UPDATE_REQUEST_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_UPDATE_RESPONSE_SNIPPET;
import project.dailyge.domain.task.TaskJpaEntity;
import project.dailyge.domain.task.TaskStatus;
import project.dailyge.domain.user.UserJpaEntity;

@DisplayName("[DocumentationTest] 할 일 수정 문서화 테스트")
class TaskUpdateDocumentationTest extends DatabaseTestBase {

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Autowired
    private TaskFacade taskFacade;

    @Test
    @DisplayName("할 일을 수정하면 200 OK 응답을 받는다.")
    void whenUpdateTaskThenStatusCodeShouldBe_200() throws Exception {
        final UserJpaEntity newUser = userWriteUseCase.save(UserFixture.createUserJpaEntity());
        final DailygeUser dailygeUser = new DailygeUser(newUser);
        final TaskRegisterRequest taskSaveRequest = new TaskRegisterRequest("주간 미팅", "Backend 팀과 Api 스펙 정의", now());
        final TaskJpaEntity newTask = taskFacade.save(taskSaveRequest.toEntity(dailygeUser));

        final TaskUpdateRequest taskUpdateRequest = new TaskUpdateRequest(
            "API 수정",
            "Backend 팀과 Api 스펙 합의",
            now(),
            TaskStatus.IN_PROGRESS
        );

        given(this.specification)
            .filter(document(IDENTIFIER,
                TASK_AUTHORIZATION_HEADER,
                TASK_UPDATE_REQUEST_SNIPPET,
                TASK_UPDATE_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, "Token")
            .header(USER_ID_KEY, newUser.getId())
            .body(objectMapper.writeValueAsString(taskUpdateRequest))
            .when()
            .put("/api/tasks/{taskId}", newTask.getId())
            .then()
            .statusCode(200);
    }
}
