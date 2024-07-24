package project.dailyge.app.test.task.documentationtest;

import static io.restassured.RestAssured.given;
import static java.util.UUID.randomUUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskReadUseCase;
import project.dailyge.app.core.task.facade.TaskFacade;
import project.dailyge.app.core.task.presentation.requesst.TaskRegisterRequest;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import static project.dailyge.app.fixture.user.UserFixture.createUserJpaEntity;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskReadSnippet.createTaskDetailSearchFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskReadSnippet.createTaskStatusesReadFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_AUTHORIZATION_HEADER;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_DATE_REQUEST_PARAMETER_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_DETAIL_SEARCH_RESPONSE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_PATH_PARAMETER_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_STATUS_READ_RESPONSE_FIELD_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.createIdentifier;
import static project.dailyge.app.test.task.fixture.TaskRequestFixture.createTaskRegisterRequest;
import project.dailyge.document.task.MonthlyTaskDocument;

import java.time.LocalDate;

@DisplayName("[DocumentationTest] Task 조회 문서화 테스트")
class TaskReadDocumentationTest extends DatabaseTestBase {

    private MonthlyTaskDocument monthlyTaskDocument;

    @Autowired
    private TaskFacade taskFacade;

    @Autowired
    private TaskReadUseCase taskReadUseCase;

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @BeforeEach
    void setUp() {
        newUser = userWriteUseCase.save(createUserJpaEntity());
        dailygeUser = new DailygeUser(newUser);
        now = LocalDate.now();

        taskFacade.createMonthlyTasks(dailygeUser, now);
        monthlyTaskDocument = taskReadUseCase.findMonthlyTaskByUserIdAndDate(dailygeUser, now);
    }

    /**
     * Task 상세 조회 문서화 테스트
     */
    @Test
    @DisplayName("[RestDocs] Task가 존재하면 200 OK 응답을 받는다.")
    void whenTaskExistsThenStatusCodeShouldBe200_OK_RestDocs() {
        final TaskRegisterRequest request = createTaskRegisterRequest(monthlyTaskDocument.getId(), now);
        final String newTaskId = taskFacade.save(dailygeUser, request.toCommand());

        given(this.specification)
            .filter(document(IDENTIFIER,
                TASK_AUTHORIZATION_HEADER,
                TASK_PATH_PARAMETER_SNIPPET,
                TASK_DATE_REQUEST_PARAMETER_SNIPPET,
                TASK_DETAIL_SEARCH_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
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
        final TaskRegisterRequest request = createTaskRegisterRequest(monthlyTaskDocument.getId(), now);
        final String newTaskId = taskFacade.save(dailygeUser, request.toCommand());
        final RestDocumentationFilter filter = createTaskDetailSearchFilter(createIdentifier("TaskDetailSearch", 200));
        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
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
        final TaskRegisterRequest request = createTaskRegisterRequest(monthlyTaskDocument.getId(), now);
        final String newTaskId = taskFacade.save(dailygeUser, request.toCommand());
        final RestDocumentationFilter filter = createTaskDetailSearchFilter(createIdentifier("TaskDetailSearch", 400));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
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
        final TaskRegisterRequest request = createTaskRegisterRequest(monthlyTaskDocument.getId(), now);
        final String newTaskId = taskFacade.save(dailygeUser, request.toCommand());
        final RestDocumentationFilter filter = createTaskDetailSearchFilter(createIdentifier("TaskDetailSearch", 404));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .param("date", now.toString())
            .when()
            .get("/api/tasks/{taskId}", randomUUID().toString())
            .then()
            .statusCode(404)
            .log()
            .all();
    }

    /**
     * Task 상태 목록 조회 문서화 테스트
     */
    @Test
    @DisplayName("Task 상태를 조회하면 200 OK를 받는다.")
    void whenReadTaskStatusResultShouldBe_200_OK_RestDocs() {
        given(this.specification)
            .filter(document(IDENTIFIER,
                TASK_AUTHORIZATION_HEADER,
                TASK_STATUS_READ_RESPONSE_FIELD_SNIPPET)
            )
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .when()
            .get("/api/tasks/status")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("Task 상태를 조회하면 200 OK를 받는다.")
    void whenReadTaskStatusResultShouldBe_200_OK_Swagger() {
        final RestDocumentationFilter filter = createTaskStatusesReadFilter(
            createIdentifier("TaskStatusList", 200), "TaskStatusListResponse"
        );
        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .when()
            .get("/api/tasks/status")
            .then()
            .statusCode(200)
            .log()
            .all();
    }
}
