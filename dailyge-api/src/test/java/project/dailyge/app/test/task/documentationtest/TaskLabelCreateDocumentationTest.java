package project.dailyge.app.test.task.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.task.application.TaskWriteService;
import project.dailyge.app.core.task.application.command.TaskLabelCreateCommand;
import project.dailyge.app.core.task.presentation.requesst.TaskLabelCreateRequest;
import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskCreateSnippet.createTaskLabelsFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_LABEL_CREATE_REQUEST_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_LABEL_CREATE_RESPONSE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.createIdentifier;

@DisplayName("[DocumentationTest] TaskLabel 등록 문서화 테스트")
class TaskLabelCreateDocumentationTest extends DatabaseTestBase {

    @Autowired
    private TaskWriteService taskWriteService;

    @Test
    @DisplayName("[Restdocs] Task Label을 생성하면, 201 Created 응답을 반환한다.")
    void whenCreateTaskLabelThenStatusCodeShouldBe_200_Restdocs() throws JsonProcessingException {
        final TaskLabelCreateRequest request = new TaskLabelCreateRequest("개발", "개발 관련 일정", "01153E");

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                TASK_LABEL_CREATE_REQUEST_SNIPPET,
                TASK_LABEL_CREATE_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/task-labels")
            .then()
            .statusCode(201);
    }

    @Test
    @DisplayName("[Swagger] Task Label을 생성하면, 201 Created 응답을 반환한다.")
    void whenCreateTaskLabelThenStatusCodeShouldBe_200_Swagger() throws JsonProcessingException {
        final TaskLabelCreateRequest request = new TaskLabelCreateRequest("개발", "개발 관련 일정", "01153E");
        final RestDocumentationFilter filter = createTaskLabelsFilter(createIdentifier("TaskLabelCreate", 201));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/task-labels")
            .then()
            .statusCode(201);
    }

    @Test
    @DisplayName("[Swagger] 최대 라벨 개수 이상 등록하면, 400 Bad Request 응답을 반환한다.")
    void whenCreateTaskLabelThenStatusCodeShouldBe_400_Swagger() throws JsonProcessingException {
        final TaskLabelCreateRequest request = new TaskLabelCreateRequest("개발", "개발 관련 일정", "01153E");
        final TaskLabelCreateCommand command = request.toCommand();
        for (int i = 0; i < 5; i++) {
            taskWriteService.saveTaskLabel(dailygeUser, command);
        }
        final RestDocumentationFilter filter = createTaskLabelsFilter(createIdentifier("TaskLabelCreate", 400));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/task-labels")
            .then()
            .statusCode(400);
    }
}
