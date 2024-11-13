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

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskRecurrenceReadSnippet.createTaskRecurrenceFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.createIdentifier;
import static project.dailyge.app.test.task.fixture.TaskRecurrenceCommandFixture.createTaskRecurrenceCreateCommand;

@DisplayName("[DocumentationTest] TaskRecurrence 조회 문서화 테스트")
class TaskRecurrenceReadDocumentationTest extends DatabaseTestBase {

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
        taskRecurrenceWriteService.save(dailygeUser, createCommand);
    }

    @Test
    @DisplayName("[RestDocs] 사용자의 반복일정을 등록하면 200 Created 응답을 받는다.")
    void whenReadRecurrenceTasksThenStatusCodeShouldBe200RestDocs() throws Exception {
        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                TASK_ACCESS_TOKEN_COOKIE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .get("/api/task-recurrences")
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("[Swagger] 사용자의 반복일정을 등록하면 200 Created 응답을 받는다.")
    void whenReadRecurrenceTasksThenStatusCodeShouldBe200Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = createTaskRecurrenceFilter(createIdentifier("TaskRecurrenceRead", 200));
        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .get("/api/task-recurrences")
            .then()
            .statusCode(200);
    }
}
