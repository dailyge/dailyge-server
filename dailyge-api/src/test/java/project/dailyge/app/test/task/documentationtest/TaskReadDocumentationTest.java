package project.dailyge.app.test.task.documentationtest;

import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.task.application.TaskWriteService;
import project.dailyge.app.core.task.facade.TaskFacade;
import project.dailyge.app.core.task.presentation.requesst.TaskCreateRequest;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskReadSnippet.createMonthlyTasksSearchWithIdFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskReadSnippet.createTaskDetailSearchFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskReadSnippet.createTaskStatusesReadFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.DATE_SEARCH_QUERY_PARAMETER_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASKS_SEARCH_RESPONSE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_DATE_REQUEST_PARAMETER_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_DETAIL_SEARCH_RESPONSE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_PATH_PARAMETER_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_STATUS_READ_RESPONSE_FIELD_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.createIdentifier;
import static project.dailyge.app.test.task.fixture.TaskRequestFixture.createTaskRegisterRequest;

import java.time.LocalDate;

@DisplayName("[DocumentationTest] Task 조회 문서화 테스트")
class TaskReadDocumentationTest extends DatabaseTestBase {

    @Autowired
    private TaskFacade taskFacade;

    @Autowired
    private TaskWriteService taskWriteService;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
        taskFacade.createMonthlyTasks(dailygeUser, now);
    }

    /**
     * Task 상세 조회 문서화 테스트
     */
    @Test
    @DisplayName("[RestDocs] Task가 존재하면 200 OK 응답을 받는다.")
    void whenTaskExistsThenStatusCodeShouldBe200_OK_RestDocs() {
        final TaskCreateRequest request = createTaskRegisterRequest(now);
        final Long newTaskId = taskWriteService.save(dailygeUser, request.toCommand());

        given(this.specification)
            .filter(document(IDENTIFIER,
                TASK_ACCESS_TOKEN_COOKIE_SNIPPET,
                TASK_PATH_PARAMETER_SNIPPET,
                TASK_DATE_REQUEST_PARAMETER_SNIPPET,
                TASK_DETAIL_SEARCH_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .param("date", now.toString())
            .when()
            .get("/api/tasks/{taskId}", newTaskId)
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] Task가 존재하면 200 OK 응답을 받는다.")
    void whenTaskExistsThenStatusCodeShouldBe200_OK_Swagger() {
        final TaskCreateRequest request = createTaskRegisterRequest(now);
        final Long newTaskId = taskWriteService.save(dailygeUser, request.toCommand());
        final RestDocumentationFilter filter = createTaskDetailSearchFilter(createIdentifier("TaskDetailSearch", 200));
        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .param("date", now.toString())
            .when()
            .get("/api/tasks/{taskId}", newTaskId)
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 TaskId라면 400 Bad Request 응답을 받는다.")
    void whenInvalidTaskIdThenStatusCodeShouldBe_400_Swagger() {
        final TaskCreateRequest request = createTaskRegisterRequest(now);
        taskWriteService.save(dailygeUser, request.toCommand());
        final RestDocumentationFilter filter = createTaskDetailSearchFilter(createIdentifier("TaskDetailSearch", 400));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .param("date", now.toString())
            .when()
            .get("/api/tasks/{taskId}", "abcd-1234")
            .then()
            .statusCode(400)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] Task가 존재하지 않는다면 404 NotFound 응답을 받는다.")
    void whenTaskNotExistsThenStatusCodeShouldBe_404_Swagger() {
        final TaskCreateRequest request = createTaskRegisterRequest(now);
        taskWriteService.save(dailygeUser, request.toCommand());
        final RestDocumentationFilter filter = createTaskDetailSearchFilter(createIdentifier("TaskDetailSearch", 404));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .param("date", now.toString())
            .when()
            .get("/api/tasks/{taskId}", Long.MAX_VALUE)
            .then()
            .statusCode(404)
            .log()
            .all();
    }

    // MonthlyTaskId 조회
    @Test
    @DisplayName("[RestDocs] 월간 일정 ID를 조회하면 200 OK 응답을 받는다.")
    void whenMonthlyTaskExistsThenStatusCodeShouldBe_200_RestDocs() {
        given(this.specification)
            .contentType(APPLICATION_JSON_VALUE)
            .param("date", now.toString())
            .cookie(getAccessTokenCookie())
            .when()
            .get("/api/monthly-tasks/id")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[RestDocs] 월간 일정을 조회하면 200 OK 응답을 받는다.")
    void whenSearchTasksWithBetweenStartDateAndEndDateThenStatusCodeShouldBe_200_OK_RestDocs() {
        final LocalDate startTime = now;
        final LocalDate endTime = now.plusDays(10);
        final TaskCreateRequest request = createTaskRegisterRequest(now);
        taskWriteService.save(dailygeUser, request.toCommand());

        given(this.specification)
            .filter(document(IDENTIFIER,
                TASK_ACCESS_TOKEN_COOKIE_SNIPPET,
                DATE_SEARCH_QUERY_PARAMETER_SNIPPET,
                TASKS_SEARCH_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .param("startDate", startTime.toString())
            .param("endDate", endTime.toString())
            .cookie(getAccessTokenCookie())
            .when()
            .get("/api/tasks")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 월간 일정을 조회하면 200 OK 응답을 받는다.")
    void whenSearchTasksWithBetweenStartDateAndEndDateThenStatusCodeShouldBe_200_OK_Swagger() {
        final LocalDate startTime = now;
        final LocalDate endTime = now.plusDays(10);
        final TaskCreateRequest request = createTaskRegisterRequest(now);
        taskWriteService.save(dailygeUser, request.toCommand());
        final RestDocumentationFilter filter = createMonthlyTasksSearchWithIdFilter(
            createIdentifier("MonthlyTaskDetailSearch", 200)
        );

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .param("startDate", startTime.toString())
            .param("endDate", endTime.toString())
            .cookie(getAccessTokenCookie())
            .when()
            .get("/api/tasks")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 파라미터로 월간 일정을 조회하면 400 Bad Request 응답을 받는다.")
    void whenSearchTasksWithBetweenStartDateAndEndDateThenStatusCodeShouldBe_400_Bad_Request_Swagger() {
        final LocalDate startTime = now;
        final LocalDate endTime = now.plusDays(10);
        final TaskCreateRequest request = createTaskRegisterRequest(now);
        taskWriteService.save(dailygeUser, request.toCommand());
        final RestDocumentationFilter filter = createMonthlyTasksSearchWithIdFilter(
            createIdentifier("MonthlyTaskDetailSearch", 400)
        );

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .param("startDate", endTime.toString())
            .param("endDate", startTime.toString())
            .cookie(getAccessTokenCookie())
            .when()
            .get("/api/tasks")
            .then()
            .statusCode(400)
            .log()
            .all();
    }

    /**
     * Task 상태 목록 조회 문서화 테스트
     */
    @Test
    @DisplayName("[RestDocs] Task 상태를 조회하면 200 OK를 받는다.")
    void whenReadTaskStatusResultShouldBe_200_OK_RestDocs() {
        given(this.specification)
            .filter(document(IDENTIFIER,
                TASK_ACCESS_TOKEN_COOKIE_SNIPPET,
                TASK_STATUS_READ_RESPONSE_FIELD_SNIPPET)
            )
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .get("/api/tasks/status")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] Task 상태를 조회하면 200 OK를 받는다.")
    void whenReadTaskStatusResultShouldBe_200_OK_Swagger() {
        final RestDocumentationFilter filter = createTaskStatusesReadFilter(createIdentifier("TaskStatusList", 200));
        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .get("/api/tasks/status")
            .then()
            .statusCode(200)
            .log()
            .all();
    }
}
