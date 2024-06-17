package project.dailyge.app.test.task.documentationtest;

import static io.restassured.RestAssured.given;
import static java.time.LocalDate.now;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.task.dto.requesst.TaskRegisterRequest;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.fixture.user.UserFixture;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_AUTHORIZATION_HEADER;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_CREATE_REQUEST_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_CREATE_RESPONSE_SNIPPET;
import project.dailyge.domain.user.UserJpaEntity;

@DisplayName("[DocumentationTest] 할 일 등록 문서화 테스트")
class TaskRegisterDocumentationTest extends DatabaseTestBase {

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Test
    @DisplayName("할 일을 등록하면 201 Created 응답을 받는다.")
    void whenSaveTaskThenStatusCodeShouldBe_201() throws Exception {
        final UserJpaEntity newUser = userWriteUseCase.save(UserFixture.createUserJpaEntity());
        final TaskRegisterRequest request = new TaskRegisterRequest("주간 미팅", "Backend 팀과 Api 스펙 정의", now());

        given(this.specification)
            .filter(document(IDENTIFIER,
                TASK_AUTHORIZATION_HEADER,
                TASK_CREATE_REQUEST_SNIPPET,
                TASK_CREATE_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, "Token")
            .header(USER_ID_KEY, newUser.getId())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/tasks")
            .then()
            .statusCode(201);
    }
}
