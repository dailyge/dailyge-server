package project.dailyge.app.test.task.documentationtest;

import static io.restassured.RestAssured.given;
import static java.time.LocalDate.now;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.core.task.dto.requesst.TaskRegisterRequest;
import project.dailyge.app.core.task.facade.TaskFacade;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import static project.dailyge.app.fixture.user.UserFixture.createUserJpaEntity;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_AUTHORIZATION_HEADER;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_DETAIL_SEARCH_PATH_PARAMETER_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_DETAIL_SEARCH_RESPONSE_SNIPPET;
import project.dailyge.domain.task.TaskJpaEntity;
import project.dailyge.domain.user.UserJpaEntity;

@DisplayName("[DocumentationTest] 할 일 저장 문서화 테스트")
class TaskDetailSearchDocumentationTest extends DatabaseTestBase {

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private TaskFacade taskFacade;

    @Test
    @DisplayName("Task가 존재하면 200 OK 응답을 받는다.")
    void whenTaskExistsThenStatusCodeShouldBe200_OK() {
        final UserJpaEntity newUser = userWriteUseCase.save(createUserJpaEntity());
        final TaskRegisterRequest request = new TaskRegisterRequest("주간 미팅", "Backend 팀과 Api 스펙 정의", now());
        DailygeToken token = tokenProvider.createToken(newUser);
        final DailygeUser dailygeUser = new DailygeUser(newUser);
        final TaskJpaEntity newTaskId = taskFacade.save(request.toEntity(dailygeUser));

        given(this.specification)
            .filter(document(IDENTIFIER,
                TASK_AUTHORIZATION_HEADER,
                TASK_DETAIL_SEARCH_PATH_PARAMETER_SNIPPET,
                TASK_DETAIL_SEARCH_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, token.getAuthorizationToken())
            .header(USER_ID_KEY, newUser.getId())
            .when()
            .get("/api/tasks/{taskId}", newTaskId.getId())
            .then()
            .statusCode(200)
            .log()
            .all();
    }
}
