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
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskReadUseCase;
import project.dailyge.app.core.task.facade.TaskFacade;
import project.dailyge.app.core.task.presentation.requesst.TaskCreateRequest;
import project.dailyge.app.core.task.presentation.requesst.TaskStatusUpdateRequest;
import project.dailyge.app.core.task.presentation.requesst.TaskUpdateRequest;
import static project.dailyge.app.test.user.fixture.UserFixture.createUserJpaEntity;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_AUTHORIZATION_HEADER;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_PATH_PARAMETER_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_STATUS_UPDATE_REQUEST_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_STATUS_UPDATE_RESPONSE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_UPDATE_REQUEST_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_UPDATE_RESPONSE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.createIdentifier;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskUpdateSnippet.createTaskStatusUpdateErrorFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskUpdateSnippet.createTaskStatusUpdateFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskUpdateSnippet.createTaskUpdateErrorFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskUpdateSnippet.createTaskUpdateFilter;
import static project.dailyge.app.test.task.fixture.TaskRequestFixture.createTaskStatusUpdateRequest;
import static project.dailyge.app.test.task.fixture.TaskRequestFixture.createTaskUpdateRequest;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;
import project.dailyge.document.task.MonthlyTaskDocument;
import static project.dailyge.entity.task.TaskStatus.IN_PROGRESS;

import java.time.LocalDate;

@DisplayName("[DocumentationTest] Task 수정 문서화 테스트")
class TaskUpdateDocumentationTest extends DatabaseTestBase {

    private final String taskUpdateResponseSchema = "TaskUpdateResponse";
    private final String taskStatusUpdateResponseSchema = "TaskStatusUpdateResponse";

    private MonthlyTaskDocument monthlyTaskDocument;

    @Autowired
    private TaskFacade taskFacade;

    @Autowired
    private TaskReadUseCase taskReadUseCase;

    @BeforeEach
    void setUp() {
        newUser = userWriteUseCase.save(createUserJpaEntity());
        dailygeUser = new DailygeUser(newUser.getId(), newUser.getRole());
        now = LocalDate.now();

        taskFacade.createMonthlyTasks(dailygeUser, now);
        monthlyTaskDocument = taskReadUseCase.findMonthlyTaskByUserIdAndDate(dailygeUser, now);
    }

    @Test
    @DisplayName("Task를 수정하면 200 OK 응답을 받는다.")
    void whenUpdateTaskThenStatusCodeShouldBe_200_RestDocs() throws Exception {
        final TaskCreateRequest taskCreateRequest = new TaskCreateRequest(
            monthlyTaskDocument.getId(), "주간 미팅", "Backend 팀과 Api 스펙 정의", now
        );
        final String newTaskId = taskFacade.save(dailygeUser, taskCreateRequest.toCommand());
        final TaskUpdateRequest taskUpdateRequest = createTaskUpdateRequest(monthlyTaskDocument.getId(), now);

        given(this.specification)
            .filter(document(IDENTIFIER,
                TASK_AUTHORIZATION_HEADER,
                TASK_PATH_PARAMETER_SNIPPET,
                TASK_UPDATE_REQUEST_SNIPPET,
                TASK_UPDATE_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .header(USER_ID_KEY, newUser.getId())
            .body(objectMapper.writeValueAsString(taskUpdateRequest))
            .when()
            .put("/api/tasks/{taskId}", newTaskId)
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("Task를 수정하면 200 OK 응답을 받는다.")
    void whenUpdateTaskThenStatusCodeShouldBe_200_Swagger() throws Exception {
        final TaskCreateRequest taskCreateRequest = new TaskCreateRequest(
            monthlyTaskDocument.getId(), "주간 미팅", "Backend 팀과 Api 스펙 정의", now
        );
        final String newTaskId = taskFacade.save(dailygeUser, taskCreateRequest.toCommand());
        final TaskUpdateRequest taskUpdateRequest = createTaskUpdateRequest(monthlyTaskDocument.getId(), now);
        final RestDocumentationFilter filter = createTaskUpdateFilter(
            createIdentifier("TaskUpdate", 200), TaskUpdateRequest.class.getSimpleName(), taskUpdateResponseSchema
        );

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .header(USER_ID_KEY, newUser.getId())
            .body(objectMapper.writeValueAsString(taskUpdateRequest))
            .when()
            .put("/api/tasks/{taskId}", newTaskId)
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("Task를 수정할 때, 올바른 필드를 입력하지 않으면 400 Bad Request 응답을 받는다.")
    void whenUpdateTaskWithInvalidFieldsThenStatusCodeShouldBe_400_Swagger() throws Exception {
        final TaskCreateRequest taskCreateRequest = new TaskCreateRequest(
            monthlyTaskDocument.getId(), "주간 미팅", "Backend 팀과 Api 스펙 정의", now
        );
        final String newTaskId = taskFacade.save(dailygeUser, taskCreateRequest.toCommand());
        final TaskUpdateRequest taskUpdateRequest = new TaskUpdateRequest(
            null,
            "Api 스펙 수정",
            "Backend 팀과 Api 스펙 수정 회의",
            now,
            IN_PROGRESS
        );
        final RestDocumentationFilter filter = createTaskUpdateErrorFilter(
            createIdentifier("TaskUpdate", 400), TaskUpdateRequest.class.getSimpleName(), taskUpdateResponseSchema
        );

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .body(objectMapper.writeValueAsString(taskUpdateRequest))
            .when()
            .put("/api/tasks/{taskId}", newTaskId)
            .then()
            .statusCode(400);
    }

    @Test
    @DisplayName("존재하지 않는 Task를 수정하면 404 Not Found 응답을 받는다.")
    void whenUpdateNotExistsTaskThenStatusCodeShouldBe_404_Swagger() throws Exception {
        final String invalidTaskId = createTimeBasedUUID();
        final TaskUpdateRequest taskUpdateRequest = createTaskUpdateRequest(invalidTaskId, now);
        final RestDocumentationFilter filter = createTaskUpdateErrorFilter(
            createIdentifier("TaskUpdate", 404), TaskUpdateRequest.class.getSimpleName(), taskUpdateResponseSchema
        );

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .body(objectMapper.writeValueAsString(taskUpdateRequest))
            .when()
            .put("/api/tasks/{taskId}", invalidTaskId)
            .then()
            .statusCode(404);
    }

    @Test
    @DisplayName("Task 상태를 수정하면 200 OK 응답을 받는다.")
    void whenUpdateTaskStatusThenStatusCodeShouldBe_200_RestDocs() throws Exception {
        final TaskCreateRequest taskCreateRequest = new TaskCreateRequest(
            monthlyTaskDocument.getId(), "주간 미팅", "Backend 팀과 Api 스펙 정의", now
        );
        final String newTaskId = taskFacade.save(dailygeUser, taskCreateRequest.toCommand());
        final TaskStatusUpdateRequest request = createTaskStatusUpdateRequest(monthlyTaskDocument.getId());

        given(this.specification)
            .filter(document(IDENTIFIER,
                TASK_AUTHORIZATION_HEADER,
                TASK_PATH_PARAMETER_SNIPPET,
                TASK_STATUS_UPDATE_REQUEST_SNIPPET,
                TASK_STATUS_UPDATE_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .put("/api/tasks/{taskId}/status", newTaskId)
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("Task 상태를 수정하면 200 OK 응답을 받는다.")
    void whenUpdateTaskStatusThenStatusCodeShouldBe_200_Swagger() throws Exception {
        final TaskCreateRequest taskCreateRequest = new TaskCreateRequest(
            monthlyTaskDocument.getId(), "주간 미팅", "Backend 팀과 Api 스펙 정의", now
        );
        final String newTaskId = taskFacade.save(dailygeUser, taskCreateRequest.toCommand());
        final TaskStatusUpdateRequest request = createTaskStatusUpdateRequest(monthlyTaskDocument.getId());
        final RestDocumentationFilter filter = createTaskStatusUpdateFilter(
            createIdentifier("TaskStatusUpdate", 200), TaskStatusUpdateRequest.class.getSimpleName(), taskStatusUpdateResponseSchema
        );

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .put("/api/tasks/{taskId}/status", newTaskId)
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("Task 상태를 수정할 때, 올바르지 않은 파라미터를 입력하면 400 Bad Request 응답을 받는다.")
    void whenUpdateTaskStatusWithInvalidParameterThenStatusCodeShouldBe_400_Swagger() throws Exception {
        final TaskCreateRequest taskCreateRequest = new TaskCreateRequest(
            monthlyTaskDocument.getId(), "주간 미팅", "Backend 팀과 Api 스펙 정의", now
        );
        final String newTaskId = taskFacade.save(dailygeUser, taskCreateRequest.toCommand());
        final TaskStatusUpdateRequest request = createTaskStatusUpdateRequest(monthlyTaskDocument.getId(), null);
        final RestDocumentationFilter filter = createTaskStatusUpdateErrorFilter(
            createIdentifier("TaskStatusUpdate", 400), TaskStatusUpdateRequest.class.getSimpleName(), taskStatusUpdateResponseSchema
        );

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .put("/api/tasks/{taskId}/status", newTaskId)
            .then()
            .statusCode(400);
    }

    @Test
    @DisplayName("Task 상태를 수정할 때, 존재하지 않는 taskId를 입력하면 404 Not Found 응답을 받는다.")
    void whenUpdateTaskStatusWithInvalidTaskIdThenStatusCodeShouldBe_404_Swagger() throws Exception {
        final TaskCreateRequest taskCreateRequest = new TaskCreateRequest(
            monthlyTaskDocument.getId(), "주간 미팅", "Backend 팀과 Api 스펙 정의", now
        );
        final String newTaskId = taskFacade.save(dailygeUser, taskCreateRequest.toCommand());
        final String invalidUUID = "abcd-15";
        final TaskStatusUpdateRequest request = createTaskStatusUpdateRequest(monthlyTaskDocument.getId());
        final RestDocumentationFilter filter = createTaskStatusUpdateErrorFilter(
            createIdentifier("TaskStatusUpdate", 404), TaskStatusUpdateRequest.class.getSimpleName(), taskStatusUpdateResponseSchema
        );

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .put("/api/tasks/{taskId}/status", invalidUUID)
            .then()
            .statusCode(404);
    }
}
