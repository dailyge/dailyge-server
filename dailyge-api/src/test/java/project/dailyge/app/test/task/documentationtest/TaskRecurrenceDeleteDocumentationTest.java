package project.dailyge.app.test.task.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.task.application.TaskRecurrenceWriteService;
import project.dailyge.app.core.task.application.TaskWriteService;
import project.dailyge.app.core.task.application.command.TaskRecurrenceCreateCommand;
import project.dailyge.app.core.task.presentation.requesst.TaskRecurrenceUpdateRequest;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskRecurrenceUpdateSnippet.createTaskRecurrenceFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_RECURRENCE_PATH_PARAMETER_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.createIdentifier;
import static project.dailyge.app.test.task.fixture.TaskRecurrenceCommandFixture.createTaskRecurrenceCreateCommand;

@DisplayName("[DocumentationTest] TaskRecurrence 삭제 문서화 테스트")
class TaskRecurrenceDeleteDocumentationTest extends DatabaseTestBase {

    @Autowired
    private TaskWriteService taskWriteService;

    @Autowired
    private TaskRecurrenceWriteService taskRecurrenceWriteService;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nowDate;
    private TaskRecurrenceCreateCommand createCommand;

    @BeforeEach
    void setUp() {
        taskWriteService.saveAll(dailygeUser, LocalDate.now());
        nowDate = LocalDate.now();
        startDate = nowDate.minusMonths(3);
        endDate = startDate.plusMonths(6);
        createCommand = createTaskRecurrenceCreateCommand(startDate, endDate, dailygeUser);
    }

    @Test
    @DisplayName("[RestDocs] 반복일정을 삭제하면 204 OK 응답을 받는다.")
    void whenDeleteRecurrenceTaskThenStatusCodeShouldBe204RestDocs() {
        final Long taskRecurrenceId = taskRecurrenceWriteService.save(dailygeUser, createCommand);
        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                TASK_ACCESS_TOKEN_COOKIE_SNIPPET,
                TASK_RECURRENCE_PATH_PARAMETER_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .delete("/api/task-recurrences/{taskRecurrenceId}", taskRecurrenceId)
            .then()
            .statusCode(204);
    }

    @Test
    @DisplayName("[Swagger] 반복일정을 삭제하면 204 OK 응답을 받는다.")
    void whenDeleteRecurrenceTaskThenStatusCodeShouldBe204Swagger() throws JsonProcessingException {
        final Long taskRecurrenceId = taskRecurrenceWriteService.save(dailygeUser, createCommand);
        final RestDocumentationFilter filter = createTaskRecurrenceFilter(createIdentifier("TaskRecurrenceDelete", 204));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .delete("/api/task-recurrences/{taskRecurrenceId}", taskRecurrenceId)
            .then()
            .statusCode(204);
    }

    @Test
    @DisplayName("[Swagger] 유효하지 않은 id로 TaskRecurrence를 삭제하면 404 Bad Request 응답을 받는다.")
    void whenInvalidParameterThenStatusCodeShouldBe400Swagger() throws JsonProcessingException {
        final Long invalidTaskRecurrenceId = -1L;
        final TaskRecurrenceUpdateRequest request = new TaskRecurrenceUpdateRequest(
            "수영",
            "오전 7시 반",
            null
        );
        final RestDocumentationFilter filter = createTaskRecurrenceFilter(createIdentifier("TaskRecurrenceDelete", 404));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .delete("/api/task-recurrences/{taskRecurrenceId}", invalidTaskRecurrenceId)
            .then()
            .statusCode(404);
    }
}
