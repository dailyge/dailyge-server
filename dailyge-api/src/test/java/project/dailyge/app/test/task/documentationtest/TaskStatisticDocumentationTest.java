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
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.MONTHLY_TASKS_STATISTIC_REQUEST_PARAMETER_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.MONTHLY_TASKS_STATISTIC_RESPONSE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.MONTHLY_WEEK_TASKS_STATISTIC_REQUEST_PARAMETER_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.MONTHLY_WEEK_TASKS_STATISTIC_RESPONSE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.WEEKLY_TASKS_STATISTIC_REQUEST_PARAMETER_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.WEEKLY_TASKS_STATISTIC_RESPONSE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.createIdentifier;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskStatisticSnippet.createMonthlyTasksStatisticSearchFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskStatisticSnippet.createWeeklyTasksStatisticSearchFilter;
import static project.dailyge.app.test.task.fixture.TaskRequestFixture.createTaskRegisterRequest;

import java.time.LocalDate;

@DisplayName("[DocumentationTest] Task 조회 문서화 테스트")
class TaskStatisticDocumentationTest extends DatabaseTestBase {

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
     * Weekly Tasks 통계
     */
    @Test
    @DisplayName("[RestDocs] 주간 Tasks 통계를 조회하면 200 OK 응답을 받는다.")
    void whenSearchWeeklyTasksStatisticsThenStatusCodeShouldBe200_OK_RestDocs() {
        final TaskCreateRequest request = createTaskRegisterRequest(now);
        taskWriteService.save(dailygeUser, request.toCommand());
        final LocalDate startDate = now;
        final LocalDate endDate = now.plusDays(10);

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                TASK_ACCESS_TOKEN_COOKIE_SNIPPET,
                WEEKLY_TASKS_STATISTIC_REQUEST_PARAMETER_SNIPPET,
                WEEKLY_TASKS_STATISTIC_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .param("startDate", startDate.toString())
            .param("endDate", endDate.toString())
            .when()
            .get("/api/tasks/statistic/weekly")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 주간 Tasks 달성률을 조회하면 200 OK 응답을 받는다.")
    void whenSearchWeeklyTasksStatisticsThenStatusCodeShouldBe200_OK_Swagger() {
        final TaskCreateRequest request = createTaskRegisterRequest(now);
        taskWriteService.save(dailygeUser, request.toCommand());
        final LocalDate startDate = now;
        final LocalDate endDate = now.plusDays(10);

        final RestDocumentationFilter filter = createWeeklyTasksStatisticSearchFilter(createIdentifier("WeeklyTasksStatistic", 200));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .param("startDate", startDate.toString())
            .param("endDate", endDate.toString())
            .when()
            .get("/api/tasks/statistic/weekly")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 날짜로 주간 Tasks 달성률을 조회하면 400 Bad Request 응답을 받는다.")
    void whenSearchWeeklyTasksStatisticsWithInvalidParametersThenStatusCodeShouldBe400_OK_Swagger() {
        final TaskCreateRequest request = createTaskRegisterRequest(now);
        taskWriteService.save(dailygeUser, request.toCommand());
        final LocalDate startDate = now;
        final LocalDate endDate = now.plusDays(600);

        final RestDocumentationFilter filter = createWeeklyTasksStatisticSearchFilter(createIdentifier("WeeklyTasksStatistic", 200));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .param("startDate", startDate.toString())
            .param("endDate", endDate.toString())
            .when()
            .get("/api/tasks/statistic/weekly")
            .then()
            .statusCode(400)
            .log()
            .all();
    }

    /**
     * Monthly Tasks 통계
     */
    @Test
    @DisplayName("[RestDocs] 월간 Tasks 통계를 조회하면 200 OK 응답을 받는다.")
    void whenSearchMonthlyTasksStatisticsThenStatusCodeShouldBe200_OK_RestDocs() {
        final TaskCreateRequest request = createTaskRegisterRequest(now);
        taskWriteService.save(dailygeUser, request.toCommand());
        final LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        final LocalDate minusMonthDate = now.minusMonths(1);
        final LocalDate startDate = minusMonthDate.withDayOfMonth(1);

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                TASK_ACCESS_TOKEN_COOKIE_SNIPPET,
                MONTHLY_TASKS_STATISTIC_REQUEST_PARAMETER_SNIPPET,
                MONTHLY_TASKS_STATISTIC_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .param("startDate", startDate.toString())
            .param("endDate", endDate.toString())
            .when()
            .get("/api/tasks/statistic/monthly")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 월간 Tasks 달성률을 조회하면 200 OK 응답을 받는다.")
    void whenSearchMonthlyTasksStatisticsThenStatusCodeShouldBe200_OK_Swagger() {
        final TaskCreateRequest request = createTaskRegisterRequest(now);
        taskWriteService.save(dailygeUser, request.toCommand());
        final LocalDate startDate = now.withDayOfMonth(1);
        final LocalDate plusMonthDate = now.plusMonths(1);
        final LocalDate endDate = plusMonthDate.withDayOfMonth(plusMonthDate.lengthOfMonth());

        final RestDocumentationFilter filter = createMonthlyTasksStatisticSearchFilter(createIdentifier("MonthlyTasksStatistic", 200));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .param("startDate", startDate.toString())
            .param("endDate", endDate.toString())
            .when()
            .get("/api/tasks/statistic/monthly")
            .then()
            .statusCode(200)
            .log()
            .all();
    }


    @Test
    @DisplayName("[Swagger] 올바르지 않은 날짜로 월간 Tasks 달성률을 조회하면 400 Bad Request 응답을 받는다.")
    void whenSearchMonthlyTasksStatisticsWithInvalidParametersThenStatusCodeShouldBe400_OK_Swagger() {
        final TaskCreateRequest request = createTaskRegisterRequest(now);
        taskWriteService.save(dailygeUser, request.toCommand());
        final LocalDate startDate = now;
        final LocalDate endDate = now.plusMonths(2);

        final RestDocumentationFilter filter = createMonthlyTasksStatisticSearchFilter(createIdentifier("MonthlyTasksStatistic", 400));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .param("startDate", startDate.toString())
            .param("endDate", endDate.toString())
            .when()
            .get("/api/tasks/statistic/monthly")
            .then()
            .statusCode(400)
            .log()
            .all();
    }

    /**
     * Monthly Week Tasks 통계
     */
    @Test
    @DisplayName("[RestDocs] 월간 주차 별 Tasks 통계를 조회하면 200 OK 응답을 받는다.")
    void whenSearchMonthlyWeekTasksStatisticsThenStatusCodeShouldBe200_OK_RestDocs() {
        final TaskCreateRequest request = createTaskRegisterRequest(now);
        taskWriteService.save(dailygeUser, request.toCommand());
        final LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        final LocalDate minusMonthDate = now.minusMonths(1);
        final LocalDate startDate = minusMonthDate.withDayOfMonth(1);

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                TASK_ACCESS_TOKEN_COOKIE_SNIPPET,
                MONTHLY_WEEK_TASKS_STATISTIC_REQUEST_PARAMETER_SNIPPET,
                MONTHLY_WEEK_TASKS_STATISTIC_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .param("startDate", startDate.toString())
            .param("endDate", endDate.toString())
            .when()
            .get("/api/tasks/statistic/monthly-weeks")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 월간 주차 별 Tasks 달성률을 조회하면 200 OK 응답을 받는다.")
    void whenSearchMonthlyWeekTasksStatisticsThenStatusCodeShouldBe200_OK_Swagger() {
        final TaskCreateRequest request = createTaskRegisterRequest(now);
        taskWriteService.save(dailygeUser, request.toCommand());
        final LocalDate startDate = now.withDayOfMonth(1);
        final LocalDate plusMonthDate = now.plusMonths(1);
        final LocalDate endDate = plusMonthDate.withDayOfMonth(plusMonthDate.lengthOfMonth());

        final RestDocumentationFilter filter = createMonthlyTasksStatisticSearchFilter(createIdentifier("MonthlyWeekTasksStatistic", 200));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .param("startDate", startDate.toString())
            .param("endDate", endDate.toString())
            .when()
            .get("/api/tasks/statistic/monthly-weeks")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 날짜로 월간 주차 별 Tasks 달성률을 조회하면 400 Bad Request 응답을 받는다.")
    void whenSearchMonthlyWeekTasksStatisticsWithInvalidParametersThenStatusCodeShouldBe400_OK_Swagger() {
        final TaskCreateRequest request = createTaskRegisterRequest(now);
        taskWriteService.save(dailygeUser, request.toCommand());
        final LocalDate startDate = now;
        final LocalDate endDate = now.plusMonths(2);

        final RestDocumentationFilter filter = createMonthlyTasksStatisticSearchFilter(createIdentifier("MonthlyWeekTasksStatistic", 400));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .param("startDate", startDate.toString())
            .param("endDate", endDate.toString())
            .when()
            .get("/api/tasks/statistic/monthly-weeks")
            .then()
            .statusCode(400)
            .log()
            .all();
    }
}
