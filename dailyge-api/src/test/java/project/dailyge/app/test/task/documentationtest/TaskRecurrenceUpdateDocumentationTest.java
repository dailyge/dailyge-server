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
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskRecurrenceUpdateSnippet.createTaskRecurrenceFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_RECURRENCE_UPDATE_REQUEST_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.createIdentifier;
import static project.dailyge.entity.task.RecurrenceType.WEEKLY;
import static project.dailyge.entity.task.TaskColor.BLUE;
import static project.dailyge.entity.task.TaskColor.GRAY;

@DisplayName("[DocumentationTest] TaskRecurrence 수정 문서화 테스트")
class TaskRecurrenceUpdateDocumentationTest extends DatabaseTestBase {

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
        createCommand = getCreateCommand();
    }

    @Test
    @DisplayName("[RestDocs] 반복일정을 업데이트하면 200 OK 응답을 받는다.")
    void whenUpdateRecurrenceTaskThenStatusCodeShouldBe200RestDocs() throws Exception {
        final Long taskRecurrenceId = taskRecurrenceWriteService.save(dailygeUser, createCommand);
        final TaskRecurrenceUpdateRequest request = new TaskRecurrenceUpdateRequest(
            "수영",
            "오전 7시 반",
            BLUE
        );
        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                TASK_ACCESS_TOKEN_COOKIE_SNIPPET,
                TASK_RECURRENCE_UPDATE_REQUEST_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .put("/api/task-recurrences/{taskRecurrenceId}", taskRecurrenceId)
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("[Swagger] 반복일정을 업데이트하면 200 OK 응답을 받는다.")
    void whenUpdateRecurrenceTaskThenStatusCodeShouldBe200Swagger() throws JsonProcessingException {
        final Long taskRecurrenceId = taskRecurrenceWriteService.save(dailygeUser, createCommand);
        final TaskRecurrenceUpdateRequest request = new TaskRecurrenceUpdateRequest(
            "수영",
            "오전 7시 반",
            BLUE
        );
        final RestDocumentationFilter filter = createTaskRecurrenceFilter(createIdentifier("TaskRecurrenceUpdate", 201));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .put("/api/task-recurrences/{taskRecurrenceId}", taskRecurrenceId)
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 파라미터로 TaskRecurrence를 등록하면 400 Bad Request 응답을 받는다.")
    void whenInvalidParameterThenStatusCodeShouldBe400Swagger() throws JsonProcessingException {
        final Long taskRecurrenceId = taskRecurrenceWriteService.save(dailygeUser, createCommand);
        final TaskRecurrenceUpdateRequest request = new TaskRecurrenceUpdateRequest(
            "수영",
            "오전 7시 반",
            null
        );
        final RestDocumentationFilter filter = createTaskRecurrenceFilter(createIdentifier("TaskRecurrenceUpdate", 201));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .put("/api/task-recurrences/{taskRecurrenceId}", taskRecurrenceId)
            .then()
            .statusCode(400);
    }

    private TaskRecurrenceCreateCommand getCreateCommand() {
        return new TaskRecurrenceCreateCommand(
            "수영",
            "오전 7시 반",
            GRAY,
            WEEKLY,
            List.of(1, 3, 5),
            startDate,
            endDate,
            dailygeUser.getUserId()
        );
    }
}
