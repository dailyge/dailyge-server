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
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskReadUseCase;
import project.dailyge.app.core.task.facade.TaskFacade;
import project.dailyge.app.core.task.presentation.requesst.TaskRegisterRequest;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import static project.dailyge.app.test.user.fixture.UserFixture.createUserJpaEntity;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskReadSnippet.createMonthlyTaskDetailSearchWithIdFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskReadSnippet.createMonthlyTaskDetailSearchWithUserIdAndDateFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskReadSnippet.createMonthlyTaskIdSearchFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.MONTHLY_TASK_DATE_QUERY_PARAMETER_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.MONTHLY_TASK_ID_READ_RESPONSE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.MONTHLY_TASK_PATH_PARAMETER_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.MONTHLY_TASK_READ_RESPONSE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_AUTHORIZATION_HEADER;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.createIdentifier;
import static project.dailyge.app.test.task.fixture.TaskRequestFixture.createTaskRegisterRequest;
import project.dailyge.document.task.MonthlyTaskDocument;

import java.time.LocalDate;
import java.util.UUID;

@DisplayName("[DocumentationTest] Task 조회 문서화 테스트")
class MonthlyTaskReadDocumentationTest extends DatabaseTestBase {

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
     * MonthlyTask 조회 문서화 테스트(V1)
     * ID로 월간 일정을 조회합니다.
     */
    @Test
    @DisplayName("[RestDocs] 월간 일정표가 존재할 때, ID로 조회하면 200 OK 응답을 받는다.")
    void whenMonthlyTaskExistsWithIdThenStatusCodeShouldBe_200_OK_RestDocs() {
        final TaskRegisterRequest request = createTaskRegisterRequest(monthlyTaskDocument.getId(), now);
        taskFacade.save(dailygeUser, request.toCommand());

        given(this.specification)
            .filter(document(IDENTIFIER,
                TASK_AUTHORIZATION_HEADER,
                MONTHLY_TASK_PATH_PARAMETER_SNIPPET,
                MONTHLY_TASK_READ_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .when()
            .get("/api/monthly-tasks/{monthlyTaskId}", monthlyTaskDocument.getId())
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 월간 일정표가 존재할 때, ID로 조회하면 200 OK 응답을 받는다.")
    void whenMonthlyTaskExistsWithIdThenStatusCodeShouldBe_200_OK_Swagger() {
        final TaskRegisterRequest request = createTaskRegisterRequest(monthlyTaskDocument.getId(), now);
        taskFacade.save(dailygeUser, request.toCommand());
        final RestDocumentationFilter filter = createMonthlyTaskDetailSearchWithIdFilter(
            createIdentifier("MonthlyTaskDetailSearch", 200)
        );

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .when()
            .get("/api/monthly-tasks/{monthlyTaskId}", monthlyTaskDocument.getId())
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 Task ID로 조회하면 400 Bad Request 응답을 받는다.")
    void whenMonthlyTaskExistsWithIdThenStatusCodeShouldBe_400_Bad_Request_Swagger() {
        final TaskRegisterRequest request = createTaskRegisterRequest(monthlyTaskDocument.getId(), now);
        taskFacade.save(dailygeUser, request.toCommand());
        final RestDocumentationFilter filter = createMonthlyTaskDetailSearchWithIdFilter(
            createIdentifier("MonthlyTaskDetailSearch", 400)
        );

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .when()
            .get("/api/monthly-tasks/{monthlyTaskId}", "abcd-1234")
            .then()
            .statusCode(400)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 월간 일정표가 존재하지 않으면 404 Not Found 응답을 받는다.")
    void whenMonthlyTaskNotExistsWithIdThenStatusCodeShouldBe_404_Not_Found_Swagger() {
        final TaskRegisterRequest request = createTaskRegisterRequest(monthlyTaskDocument.getId(), now);
        taskFacade.save(dailygeUser, request.toCommand());
        final RestDocumentationFilter filter = createMonthlyTaskDetailSearchWithIdFilter(
            createIdentifier("MonthlyTaskDetailSearch", 404)
        );

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .when()
            .get("/api/monthly-tasks/{monthlyTaskId}", UUID.randomUUID().toString())
            .then()
            .statusCode(404)
            .log()
            .all();
    }

    /**
     * MonthlyTask 조회 문서화 테스트(V2)
     * 사용자 ID와 날짜로 월간 일정을 조회합니다.
     */
    @Test
    @DisplayName("사용자 ID와 날짜로 조회했을 때, 월간 일정표가 존재하면 200 OK 응답을 받는다.")
    void whenMonthlyTaskExistsWithUserIdAndDateThenStatusCodeShouldBe200_OK_RestDocs() {
        final TaskRegisterRequest request = createTaskRegisterRequest(monthlyTaskDocument.getId(), now);
        taskFacade.save(dailygeUser, request.toCommand());

        given(this.specification)
            .filter(document(IDENTIFIER,
                TASK_AUTHORIZATION_HEADER,
                MONTHLY_TASK_DATE_QUERY_PARAMETER_SNIPPET,
                MONTHLY_TASK_READ_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .param("date", now.toString())
            .when()
            .get("/api/monthly-tasks")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("사용자 ID와 날짜로 조회했을 때, 월간 일정표가 존재하면 200 OK 응답을 받는다.")
    void whenMonthlyTaskExistsWithUserIdAndDateThenStatusCodeShouldBe200_OK_Swagger() {
        final TaskRegisterRequest request = createTaskRegisterRequest(monthlyTaskDocument.getId(), now);
        taskFacade.save(dailygeUser, request.toCommand());
        final RestDocumentationFilter filter = createMonthlyTaskDetailSearchWithUserIdAndDateFilter(
            createIdentifier("MonthlyTaskSearch-V2", 200)
        );

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .param("date", now.toString())
            .when()
            .get("/api/monthly-tasks")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 파라미터로 월간 일정표를 조회하면 400 Bad Request 응답을 받는다.")
    void whenInvalidMonthlyTaskSearchWithUserIdAndDateThenStatusCodeShouldBe_400_Bad_Request_Swagger() {
        final TaskRegisterRequest request = createTaskRegisterRequest(monthlyTaskDocument.getId(), now);
        taskFacade.save(dailygeUser, request.toCommand());
        final RestDocumentationFilter filter = createMonthlyTaskDetailSearchWithUserIdAndDateFilter(
            createIdentifier("MonthlyTaskDetailSearch-V2", 400)
        );

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .param("date", "2022---1010")
            .when()
            .get("/api/monthly-tasks")
            .then()
            .statusCode(400)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 사용자 아이디와 날짜로 월간 일정표를 조회했을 때, 월간 일정표가 존재하지 않으면 404 Not Found 응답을 받는다.")
    void whenInvalidMonthlyTaskNotExistsWithUserIdAndDateThenStatusCodeShouldBe_404_Not_Found_Swagger() {
        final TaskRegisterRequest request = createTaskRegisterRequest(monthlyTaskDocument.getId(), now);
        taskFacade.save(dailygeUser, request.toCommand());
        final RestDocumentationFilter filter = createMonthlyTaskDetailSearchWithUserIdAndDateFilter(
            createIdentifier("MonthlyTaskDetailSearch-V2", 404)
        );
        final LocalDate future = LocalDate.of(2300, 10, 10);

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .param("date", future.toString())
            .when()
            .get("/api/monthly-tasks")
            .then()
            .statusCode(404)
            .log()
            .all();
    }

    /**
     * MonthlyTaskId 조회
     */
    @Test
    @DisplayName("월간 일정표ID가 존재하면 200 OK 응답을 받는다.")
    void whenMonthlyTaskIdExistsWithUserIdAndDateThenStatusCodeShouldBe200_OK_RestDocs() {
        final TaskRegisterRequest request = createTaskRegisterRequest(monthlyTaskDocument.getId(), now);
        taskFacade.save(dailygeUser, request.toCommand());

        given(this.specification)
            .filter(document(IDENTIFIER,
                TASK_AUTHORIZATION_HEADER,
                MONTHLY_TASK_DATE_QUERY_PARAMETER_SNIPPET,
                MONTHLY_TASK_ID_READ_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .param("date", now.toString())
            .when()
            .get("/api/monthly-tasks/id")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("사용자 ID와 날짜로 월간 일정표를 조회할 때, 월간 일정표 ID가 존재하면 200 OK 응답을 받는다.")
    void whenMonthlyTaskIdExistsWithUserIdAndDateThenStatusCodeShouldBe200_OK_Swagger() {
        final TaskRegisterRequest request = createTaskRegisterRequest(monthlyTaskDocument.getId(), now);
        taskFacade.save(dailygeUser, request.toCommand());
        final RestDocumentationFilter filter = createMonthlyTaskIdSearchFilter(createIdentifier("MonthlyTaskIdSearch", 200));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .param("date", now.toString())
            .when()
            .get("/api/monthly-tasks/id")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("사용자 ID와 날짜로 월간 일정표를 조회할 때, 월간 일정표가 존재하지 않으면 404 NotFound 응답을 받는다.")
    void whenMonthlyTaskIdNotExistsWithUserIdAndDateThenStatusCodeShouldBe_404_NotFound_Swagger() {
        final TaskRegisterRequest request = createTaskRegisterRequest(monthlyTaskDocument.getId(), now);
        taskFacade.save(dailygeUser, request.toCommand());
        final RestDocumentationFilter filter = createMonthlyTaskIdSearchFilter(createIdentifier("MonthlyTaskIdSearch", 404));
        final LocalDate future = LocalDate.of(2300, 7, 10);

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .param("date", future.toString())
            .when()
            .get("/api/monthly-tasks/id")
            .then()
            .statusCode(404)
            .log()
            .all();
    }
}
