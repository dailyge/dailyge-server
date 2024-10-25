package project.dailyge.app.test.monthlygoal.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.monthlygoal.application.MonthlyGoalWriteService;
import project.dailyge.app.core.monthlygoal.application.command.MonthlyGoalCreateCommand;
import project.dailyge.app.core.monthlygoal.presentation.request.MonthlyGoalUpdateRequest;
import static project.dailyge.app.test.monthlygoal.documentationtest.snippet.MonthlyGoalSnippet.MONTHLY_GOAL_ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.monthlygoal.documentationtest.snippet.MonthlyGoalSnippet.MONTHLY_GOAL_PATH_PARAMETER_SNIPPET;
import static project.dailyge.app.test.monthlygoal.documentationtest.snippet.MonthlyGoalSnippet.MONTHLY_GOAL_UPDATE_REQUEST_SNIPPET;
import static project.dailyge.app.test.monthlygoal.documentationtest.snippet.MonthlyGoalSnippet.MONTHLY_GOAL_UPDATE_RESPONSE_SNIPPET;
import static project.dailyge.app.test.monthlygoal.documentationtest.snippet.MonthlyGoalSnippet.createIdentifier;
import static project.dailyge.app.test.monthlygoal.documentationtest.snippet.MonthlyGoalUpdateSnippet.updateMonthlyGoalErrorFilter;
import static project.dailyge.app.test.monthlygoal.documentationtest.snippet.MonthlyGoalUpdateSnippet.updateMonthlyGoalFilter;

import java.time.LocalDate;

@DisplayName("[DocumentationTest] 월간 목표 수정 문서화 테스트")
class MonthlyGoalUpdateDocumentationTest extends DatabaseTestBase {

    @Autowired
    private MonthlyGoalWriteService monthlyGoalWriteService;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
    }

    @Test
    @DisplayName("[RestDocs] 월간 목표를 수정하면 200 OK 응답을 받는다.")
    void whenUpdateMonthlyGoalThenStatusCodeShouldBe200_RestDocs() throws JsonProcessingException {
        final MonthlyGoalCreateCommand createCommand = new MonthlyGoalCreateCommand("메인 페이지 개발 완료", "서비스 출시.", now);
        final Long monthlyGoalId = monthlyGoalWriteService.save(dailygeUser, createCommand);

        final MonthlyGoalUpdateRequest request = new MonthlyGoalUpdateRequest("일정 연기", "외부 협력 업체 이슈");

        given(this.specification)
            .filter(document(IDENTIFIER,
                MONTHLY_GOAL_ACCESS_TOKEN_COOKIE_SNIPPET,
                MONTHLY_GOAL_PATH_PARAMETER_SNIPPET,
                MONTHLY_GOAL_UPDATE_REQUEST_SNIPPET,
                MONTHLY_GOAL_UPDATE_RESPONSE_SNIPPET
            ))
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .put("/api/monthly-goals/{monthlyGoalId}", monthlyGoalId)
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 월간 목표를 수정하면 200 OK 응답을 받는다.")
    void whenUpdateMonthlyGoalThenStatusCodeShouldBe200_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = updateMonthlyGoalFilter(createIdentifier("MonthlyGoalUpdate", 200));
        final MonthlyGoalCreateCommand createCommand = new MonthlyGoalCreateCommand("메인 페이지 개발 완료", "서비스 출시.", now);
        final Long monthlyGoalId = monthlyGoalWriteService.save(dailygeUser, createCommand);

        final MonthlyGoalUpdateRequest request = new MonthlyGoalUpdateRequest("일정 연기", "외부 협력 업체 이슈");

        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .put("/api/monthly-goals/{monthlyGoalId}", monthlyGoalId)
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 파라미터를 입력하면 400 Bad Request 응답을 받는다.")
    void whenUpdateMonthlyGoalButInvalidParameterThenStatusCodeShouldBe400_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = updateMonthlyGoalErrorFilter(createIdentifier("MonthlyGoalUpdate", 400));
        final MonthlyGoalUpdateRequest request = new MonthlyGoalUpdateRequest("일정 연기", "외부 협력 업체 이슈");

        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .put("/api/monthly-goals/{monthlyGoalId}", "invalidId")
            .then()
            .statusCode(400)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 존재하지 않는 월간 목표 ID를 입력하면 404 Not Found 응답을 받는다.")
    void whenUpdateMonthlyGoalButNotExistsThenStatusCodeShouldBe_404() throws JsonProcessingException {
        final RestDocumentationFilter filter = updateMonthlyGoalErrorFilter(createIdentifier("MonthlyGoalUpdate", 404));
        final MonthlyGoalUpdateRequest request = new MonthlyGoalUpdateRequest("일정 연기", "외부 협력 업체 이슈");

        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .put("/api/monthly-goals/{monthlyGoalId}", 300)
            .then()
            .statusCode(404)
            .log()
            .all();
    }
}
