package project.dailyge.app.test.weeklygoal.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.weeklygoal.request.WeeklyGoalCreateRequest;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalCreateSnippet.createWeeklyGoalErrorFilter;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalCreateSnippet.createWeeklyGoalFilter;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalSnippet.WEEKLY_GOAL_ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalSnippet.WEEKLY_GOAL_CREATE_REQUEST_SNIPPET;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalSnippet.WEEKLY_GOAL_CREATE_RESPONSE_SNIPPET;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalSnippet.createIdentifier;

@DisplayName("[DocumentationTest] 주간 목표 작성 문서화 테스트")
class WeeklyGoalCreateDocumentationTest extends DatabaseTestBase {

    private WeeklyGoalCreateRequest request;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
        request = new WeeklyGoalCreateRequest("주간 목표 작성 API 개발", "원격 저장소 올리기 전에 셀프 리뷰", now);
    }

    @Test
    @DisplayName("[RestDocs] 주간 목표를 생성하면 201 Created 응답을 받는다.")
    void whenCreateWeeklyGoalThenStatusCodeShouldBe201_RestDocs() throws JsonProcessingException {
        given(this.specification)
            .filter(document(IDENTIFIER,
                WEEKLY_GOAL_ACCESS_TOKEN_COOKIE_SNIPPET,
                WEEKLY_GOAL_CREATE_REQUEST_SNIPPET,
                WEEKLY_GOAL_CREATE_RESPONSE_SNIPPET
            ))
            .relaxedHTTPSValidation()
            .body(objectMapper.writeValueAsString(request))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .post("/api/weekly-goals")
            .then()
            .statusCode(201)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 주간 목표를 생성하면 201 Created 응답을 받는다.")
    void whenCreateWeeklyGoalThenStatusCodeShouldBe201_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = createWeeklyGoalFilter(createIdentifier("WeeklyGoalCreate", 201));
        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .body(objectMapper.writeValueAsString(request))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .post("/api/weekly-goals")
            .then()
            .statusCode(201)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 주간 목표를 생성할 때, 올바르지 않은 인자를 입력하면 400 Bad Request 응답을 받는다.")
    void whenCreateWeeklyGoalWithInvalidRequestThenStatusCodeShouldBe400_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = createWeeklyGoalErrorFilter(createIdentifier("WeeklyGoalCreate", 400));
        final String invalidTitle = "47kN8pkaK2tupPHZf5D0Dj5IdY47TI60wHxR9lyDtxEQuCEhgkYGlJ7qBfZL";
        final WeeklyGoalCreateRequest invalidRequest = new WeeklyGoalCreateRequest(invalidTitle, "API 개발", now);
        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .body(objectMapper.writeValueAsString(invalidRequest))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .post("/api/weekly-goals")
            .then()
            .statusCode(400)
            .log()
            .all();
    }
}
