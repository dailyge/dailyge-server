package project.dailyge.app.test.task.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.task.facade.TaskFacade;
import project.dailyge.app.core.task.presentation.requesst.MonthlyTasksRegisterRequest;
import static project.dailyge.app.test.user.fixture.UserFixture.createUserJpaEntity;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskCreateSnippet.createMonthlyTasksErrorFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskCreateSnippet.createMonthlyTasksFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.MONTHLY_TASK_CREATE_REQUEST_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.MONTHLY_TASK_CREATE_RESPONSE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.TASK_AUTHORIZATION_HEADER;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.createIdentifier;
import project.dailyge.entity.user.UserJpaEntity;

import java.time.LocalDate;

@DisplayName("[DocumentationTest] 월간 일정표 생성 문서화 테스트")
class MonthlyTasksRegisterDocumentationTest extends DatabaseTestBase {

    private final String responseSchema = "MonthlyTasksRegisterResponse";

    @Autowired
    private TaskFacade taskFacade;

    @Test
    @DisplayName("월간 일정표를 생성하면 201 Created 응답을 받는다.")
    void whenCreateMonthlyTasksThenResponseShouldBe201_RestDocs() throws JsonProcessingException {
        final UserJpaEntity newUser = userWriteUseCase.save(createUserJpaEntity());
        final DailygeUser dailygeUser = new DailygeUser(newUser);
        final LocalDate now = LocalDate.now();
        final MonthlyTasksRegisterRequest request = new MonthlyTasksRegisterRequest(now);

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                TASK_AUTHORIZATION_HEADER,
                MONTHLY_TASK_CREATE_REQUEST_SNIPPET,
                MONTHLY_TASK_CREATE_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/monthly-tasks")
            .then()
            .statusCode(201);
    }

    @Test
    @DisplayName("월간 일정표를 생성하면 201 Created 응답을 받는다.")
    void whenCreateMonthlyTasksThenResponseShouldBe201_Swagger() throws JsonProcessingException {
        final UserJpaEntity newUser = userWriteUseCase.save(createUserJpaEntity());
        final DailygeUser dailygeUser = new DailygeUser(newUser);
        final LocalDate now = LocalDate.now();
        final MonthlyTasksRegisterRequest request = new MonthlyTasksRegisterRequest(now);
        final RestDocumentationFilter filter = createMonthlyTasksFilter(
            createIdentifier("MonthlyTaskCreate", 201), MonthlyTasksRegisterRequest.class.getSimpleName(), responseSchema
        );

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/monthly-tasks")
            .then()
            .statusCode(201);
    }

    @Test
    @DisplayName("월간 일정표를 생성할 때, 날짜를 입력하지 않으면 400 Bad Request 응답을 받는다.")
    void whenCreateMonthlyTasksMissDateThenResponseShouldBe400_Swagger() throws JsonProcessingException {
        final UserJpaEntity newUser = userWriteUseCase.save(createUserJpaEntity());
        final DailygeUser dailygeUser = new DailygeUser(newUser);
        final MonthlyTasksRegisterRequest request = new MonthlyTasksRegisterRequest(null);
        final RestDocumentationFilter filter = createMonthlyTasksErrorFilter(
            createIdentifier("MonthlyTaskCreate", 400), MonthlyTasksRegisterRequest.class.getSimpleName(), responseSchema
        );

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/monthly-tasks")
            .then()
            .statusCode(400);
    }

    @Test
    @DisplayName("월간 일정표를 생성할 때, 월간 일정표가 이미 존재한다면, 409 Conflict 응답을 받는다.")
    void whenCreateExistsMonthlyTasksDateThenResponseShouldBe409_Swagger() throws JsonProcessingException {
        final UserJpaEntity newUser = userWriteUseCase.save(createUserJpaEntity());
        final DailygeUser dailygeUser = new DailygeUser(newUser);
        final LocalDate now = LocalDate.now();
        final MonthlyTasksRegisterRequest request = new MonthlyTasksRegisterRequest(now);
        final RestDocumentationFilter filter = createMonthlyTasksErrorFilter(
            createIdentifier("MonthlyTaskCreate", 409), MonthlyTasksRegisterRequest.class.getSimpleName(), responseSchema
        );
        taskFacade.createMonthlyTasks(dailygeUser, now);

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/monthly-tasks")
            .then()
            .statusCode(409);
    }
}
