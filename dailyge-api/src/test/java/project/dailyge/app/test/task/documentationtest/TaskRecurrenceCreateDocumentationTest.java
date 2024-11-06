package project.dailyge.app.test.task.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.task.application.TaskWriteService;
import project.dailyge.app.core.task.presentation.requesst.TaskRecurrenceCreateRequest;
import project.dailyge.entity.task.RecurrenceType;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskRecurrenceCreateSnippet.createTaskRecurrenceFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_RECURRENCE_CREATE_REQUEST_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_RECURRENCE_CREATE_RESPONSE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.createIdentifier;
import static project.dailyge.entity.task.TaskColor.GRAY;

@DisplayName("[DocumentationTest] TaskRecurrence 등록 문서화 테스트")
public class TaskRecurrenceCreateDocumentationTest extends DatabaseTestBase {

    @Autowired
    private TaskWriteService taskWriteService;

    @BeforeEach
    void setUp() {
        taskWriteService.saveAll(dailygeUser, LocalDate.now());
    }

    @Test
    @DisplayName("[RestDocs] 반복일정을 등록하면 201 Created 응답을 받는다.")
    void whenRegisterRecurrenceTaskThenStatusCodeShouldBe201RestDocs() throws Exception {
        final TaskRecurrenceCreateRequest request = new TaskRecurrenceCreateRequest(
            "수영",
            "오전 8시 반",
            GRAY,
            RecurrenceType.WEEKLY,
            List.of(1, 3, 5),
            LocalDate.now(),
            LocalDate.now().plusMonths(6)
        );

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                TASK_ACCESS_TOKEN_COOKIE_SNIPPET,
                TASK_RECURRENCE_CREATE_REQUEST_SNIPPET,
                TASK_RECURRENCE_CREATE_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/task-recurrences")
            .then()
            .statusCode(201);
    }

    @Test
    @DisplayName("[Swagger] TaskRecurrence를 등록하면 201 Created 응답을 받는다.")
    void whenRegisterRecurrenceTaskThenStatusCodeShouldBe201Swagger() throws JsonProcessingException {
        final TaskRecurrenceCreateRequest request = new TaskRecurrenceCreateRequest(
            "수영",
            "오전 8시 반",
            GRAY,
            RecurrenceType.WEEKLY,
            List.of(1, 3, 5),
            LocalDate.now(),
            LocalDate.now().plusMonths(6)
        );
        final RestDocumentationFilter filter = createTaskRecurrenceFilter(createIdentifier("TaskRecurrenceCreate", 201));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/task-recurrences")
            .then()
            .statusCode(201);
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 파라미터로 TaskRecurrence를 등록하면 400 Bad Request 응답을 받는다.")
    void whenInvalidParameterThenStatusCodeShouldBe400Swagger() throws JsonProcessingException {
        final TaskRecurrenceCreateRequest request = new TaskRecurrenceCreateRequest(
            "수영",
            "오전 8시 반",
            GRAY,
            RecurrenceType.WEEKLY,
            List.of(8),
            LocalDate.now(),
            LocalDate.now().plusMonths(6)
        );
        final RestDocumentationFilter filter = createTaskRecurrenceFilter(createIdentifier("TaskRecurrenceCreate", 400));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/task-recurrences")
            .then()
            .statusCode(400);
    }
}
