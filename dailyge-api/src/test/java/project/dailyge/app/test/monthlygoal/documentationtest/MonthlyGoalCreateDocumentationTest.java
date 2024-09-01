package project.dailyge.app.test.monthlygoal.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.monthlygoal.presentation.request.MonthlyGoalCreateRequest;
import static project.dailyge.app.test.monthlygoal.documentationtest.snippet.MonthlyGoalCreateSnippet.createMonthlyGoalErrorFilter;
import static project.dailyge.app.test.monthlygoal.documentationtest.snippet.MonthlyGoalCreateSnippet.createMonthlyGoalFilter;
import static project.dailyge.app.test.monthlygoal.documentationtest.snippet.MonthlyGoalSnippet.MONTHLY_GOAL_ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.monthlygoal.documentationtest.snippet.MonthlyGoalSnippet.MONTHLY_GOAL_CREATE_REQUEST_SNIPPET;
import static project.dailyge.app.test.monthlygoal.documentationtest.snippet.MonthlyGoalSnippet.MONTHLY_GOAL_CREATE_RESPONSE_SNIPPET;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.createIdentifier;

@DisplayName("[DocumentationTest] 월간 목표 작성 문서화 테스트")
class MonthlyGoalCreateDocumentationTest extends DatabaseTestBase {

    private MonthlyGoalCreateRequest request;

    @BeforeEach
    void setUp() {
        request = new MonthlyGoalCreateRequest("메인 페이지 개발 완료", "서비스 출시.");
    }

    @Test
    @DisplayName("[RestDocs] 월간 목표를 생성하면 201 Created 응답을 받는다.")
    void whenCreateMonthlyGoalThenStatusCodeShouldBe201_RestDocs() throws JsonProcessingException {
        given(this.specification)
            .filter(document(IDENTIFIER,
                MONTHLY_GOAL_ACCESS_TOKEN_COOKIE_SNIPPET,
                MONTHLY_GOAL_CREATE_REQUEST_SNIPPET,
                MONTHLY_GOAL_CREATE_RESPONSE_SNIPPET
            ))
            .relaxedHTTPSValidation()
            .body(objectMapper.writeValueAsString(request))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .post("/api/monthly-goals")
            .then()
            .statusCode(201)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 월간 목표를 생성하면 201 Created 응답을 받는다.")
    void whenCreateMonthlyGoalThenStatusCodeShouldBe201_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = createMonthlyGoalFilter(createIdentifier("MonthlyGoalCreate", 201));
        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .body(objectMapper.writeValueAsString(request))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .post("/api/monthly-goals")
            .then()
            .statusCode(201)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 월간 목표를 생성할 때, 올바르지 않은 인자를 입력하면 400 Bad Request 응답을 받는다.")
    void whenCreateMonthlyGoalWithInvalidRequestThenStatusCodeShouldBe400_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = createMonthlyGoalErrorFilter(createIdentifier("MonthlyGoalCreate", 400));
        final MonthlyGoalCreateRequest invalidRequest = request = new MonthlyGoalCreateRequest("", "서비스 출시.");
        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .body(objectMapper.writeValueAsString(invalidRequest))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .post("/api/monthly-goals")
            .then()
            .statusCode(400)
            .log()
            .all();
    }
}
