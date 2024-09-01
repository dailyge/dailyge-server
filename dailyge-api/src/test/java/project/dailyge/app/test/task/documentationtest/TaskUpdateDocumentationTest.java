package project.dailyge.app.test.task.documentationtest;

import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.task.application.TaskReadUseCase;
import project.dailyge.app.core.task.application.TaskWriteUseCase;
import project.dailyge.app.core.task.facade.TaskFacade;
import project.dailyge.app.core.task.presentation.requesst.TaskCreateRequest;
import project.dailyge.app.core.task.presentation.requesst.TaskStatusUpdateRequest;
import project.dailyge.app.core.task.presentation.requesst.TaskUpdateRequest;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_PATH_PARAMETER_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_STATUS_UPDATE_REQUEST_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_STATUS_UPDATE_RESPONSE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_UPDATE_REQUEST_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_UPDATE_RESPONSE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.createIdentifier;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskUpdateSnippet.createTaskStatusUpdateFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskUpdateSnippet.createTaskUpdateFilter;
import static project.dailyge.app.test.task.fixture.TaskRequestFixture.createTaskStatusUpdateRequest;
import static project.dailyge.app.test.task.fixture.TaskRequestFixture.createTaskUpdateRequest;
import project.dailyge.entity.task.MonthlyTaskJpaEntity;
import static project.dailyge.entity.task.TaskColor.BLUE;
import static project.dailyge.entity.task.TaskStatus.IN_PROGRESS;

import java.time.LocalDate;

@DisplayName("[DocumentationTest] Task 수정 문서화 테스트")
class TaskUpdateDocumentationTest extends DatabaseTestBase {

    private TaskCreateRequest taskCreateRequest;
    private MonthlyTaskJpaEntity monthlyTask;

    @Autowired
    private TaskFacade taskFacade;

    @Autowired
    private TaskReadUseCase taskReadUseCase;

    @Autowired
    private TaskWriteUseCase taskWriteUseCase;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
        taskFacade.createMonthlyTasks(dailygeUser, now);
        monthlyTask = taskReadUseCase.findMonthlyTaskByUserIdAndDate(dailygeUser, now);
        taskCreateRequest = new TaskCreateRequest("주간 미팅", "Backend 팀과 Api 스펙 정의", BLUE, now);
    }

    @Test
    @DisplayName("[RestDocs] Task를 수정하면 200 OK 응답을 받는다.")
    void whenUpdateTaskThenStatusCodeShouldBe_200_RestDocs() throws Exception {
        final Long newTaskId = taskWriteUseCase.save(dailygeUser, taskCreateRequest.toCommand());
        final TaskUpdateRequest taskUpdateRequest = createTaskUpdateRequest(now);

        given(this.specification)
            .filter(document(IDENTIFIER,
                TASK_ACCESS_TOKEN_COOKIE_SNIPPET,
                TASK_PATH_PARAMETER_SNIPPET,
                TASK_UPDATE_REQUEST_SNIPPET,
                TASK_UPDATE_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .header(USER_ID_KEY, newUser.getId())
            .body(objectMapper.writeValueAsString(taskUpdateRequest))
            .when()
            .put("/api/tasks/{taskId}", newTaskId)
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("[Swagger] Task를 수정하면 200 OK 응답을 받는다.")
    void whenUpdateTaskThenStatusCodeShouldBe_200_Swagger() throws Exception {
        final Long newTaskId = taskWriteUseCase.save(dailygeUser, taskCreateRequest.toCommand());
        final TaskUpdateRequest taskUpdateRequest = createTaskUpdateRequest(now);
        final RestDocumentationFilter filter = createTaskUpdateFilter(createIdentifier("TaskUpdate", 200));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .header(USER_ID_KEY, newUser.getId())
            .body(objectMapper.writeValueAsString(taskUpdateRequest))
            .when()
            .put("/api/tasks/{taskId}", newTaskId)
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("[Swagger] Task를 수정할 때, 올바른 필드를 입력하지 않으면 400 Bad Request 응답을 받는다.")
    void whenUpdateTaskWithInvalidFieldsThenStatusCodeShouldBe_400_Swagger() throws Exception {
        final Long newTaskId = taskWriteUseCase.save(dailygeUser, taskCreateRequest.toCommand());
        final TaskUpdateRequest taskUpdateRequest = new TaskUpdateRequest(
            "Api 스펙 수정",
            null,
            now,
            IN_PROGRESS,
            BLUE
        );
        final RestDocumentationFilter filter = createTaskUpdateFilter(createIdentifier("TaskUpdate", 400));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(taskUpdateRequest))
            .when()
            .put("/api/tasks/{taskId}", newTaskId)
            .then()
            .statusCode(400);
    }

    @Test
    @DisplayName("[Swagger] 존재하지 않는 Task를 수정하려고 하면 404 Not Found 응답을 받는다.")
    void whenUpdateNotExistsTaskThenStatusCodeShouldBe_404_Swagger() throws Exception {
        final Long invalidTaskId = Long.MAX_VALUE;
        final TaskUpdateRequest taskUpdateRequest = createTaskUpdateRequest(now);
        final RestDocumentationFilter filter = createTaskUpdateFilter(createIdentifier("TaskUpdate", 404));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(taskUpdateRequest))
            .when()
            .put("/api/tasks/{taskId}", invalidTaskId)
            .then()
            .statusCode(404);
    }

    @Test
    @DisplayName("[RestDocs] Task 상태를 수정하면 200 OK 응답을 받는다.")
    void whenUpdateTaskStatusThenStatusCodeShouldBe_200_RestDocs() throws Exception {
        final Long newTaskId = taskWriteUseCase.save(dailygeUser, taskCreateRequest.toCommand());
        final TaskStatusUpdateRequest request = createTaskStatusUpdateRequest(now);

        given(this.specification)
            .filter(document(IDENTIFIER,
                TASK_ACCESS_TOKEN_COOKIE_SNIPPET,
                TASK_PATH_PARAMETER_SNIPPET,
                TASK_STATUS_UPDATE_REQUEST_SNIPPET,
                TASK_STATUS_UPDATE_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .patch("/api/tasks/{taskId}/status", newTaskId)
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("[Swagger] Task 상태를 수정하면 200 OK 응답을 받는다.")
    void whenUpdateTaskStatusThenStatusCodeShouldBe_200_Swagger() throws Exception {
        final Long newTaskId = taskWriteUseCase.save(dailygeUser, taskCreateRequest.toCommand());
        final TaskStatusUpdateRequest request = createTaskStatusUpdateRequest(now);
        final RestDocumentationFilter filter = createTaskStatusUpdateFilter(createIdentifier("TaskStatusUpdate", 200));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .patch("/api/tasks/{taskId}/status", newTaskId)
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("[Swagger] Task 상태를 수정할 때, 올바르지 않은 파라미터를 입력하면 400 Bad Request 응답을 받는다.")
    void whenUpdateTaskStatusWithInvalidParameterThenStatusCodeShouldBe_400_Swagger() throws Exception {
        final Long newTaskId = taskWriteUseCase.save(dailygeUser, taskCreateRequest.toCommand());
        final TaskStatusUpdateRequest request = createTaskStatusUpdateRequest(null);
        final RestDocumentationFilter filter = createTaskStatusUpdateFilter(createIdentifier("TaskStatusUpdate", 400));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .patch("/api/tasks/{taskId}/status", newTaskId)
            .then()
            .statusCode(400);
    }

    @Test
    @DisplayName("[Swagger] Task 상태를 수정할 때, 존재하지 않는 taskId를 입력하면 404 Not Found 응답을 받는다.")
    void whenUpdateTaskStatusWithInvalidTaskIdThenStatusCodeShouldBe_404_Swagger() throws Exception {
        taskWriteUseCase.save(dailygeUser, taskCreateRequest.toCommand());
        final Long invalidUUID = Long.MAX_VALUE;
        final TaskStatusUpdateRequest request = createTaskStatusUpdateRequest(now);
        final RestDocumentationFilter filter = createTaskStatusUpdateFilter(createIdentifier("TaskStatusUpdate", 404));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .patch("/api/tasks/{taskId}/status", invalidUUID)
            .then()
            .statusCode(404);
    }
}
