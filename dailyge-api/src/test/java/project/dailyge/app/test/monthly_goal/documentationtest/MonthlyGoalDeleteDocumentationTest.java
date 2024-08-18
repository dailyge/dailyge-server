package project.dailyge.app.test.monthly_goal.documentationtest;

import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.monthly_goal.application.MonthlyGoalWriteUseCase;
import project.dailyge.app.core.monthly_goal.application.command.MonthlyGoalCreateCommand;
import static project.dailyge.app.test.monthly_goal.documentationtest.snippet.MonthlyGoalDeleteSnippet.deleteMonthlyGoalErrorFilter;
import static project.dailyge.app.test.monthly_goal.documentationtest.snippet.MonthlyGoalDeleteSnippet.deleteMonthlyGoalFilter;
import static project.dailyge.app.test.monthly_goal.documentationtest.snippet.MonthlyGoalSnippet.MONTHLY_GOAL_AUTHORIZATION_HEADER;
import static project.dailyge.app.test.monthly_goal.documentationtest.snippet.MonthlyGoalSnippet.MONTHLY_GOAL_PATH_PARAMETER_SNIPPET;
import static project.dailyge.app.test.monthly_goal.documentationtest.snippet.MonthlyGoalSnippet.createIdentifier;

@DisplayName("[DocumentationTest] 월간 목표 삭제 문서화 테스트")
class MonthlyGoalDeleteDocumentationTest extends DatabaseTestBase {

    @Autowired
    private MonthlyGoalWriteUseCase monthlyGoalWriteUseCase;

    @Test
    @DisplayName("[RestDocs] 월간 목표를 삭제하면 204 No-Content 응답을 받는다.")
    void whenDeleteMonthlyGoalThenStatusCodeShouldBe204_RestDocs() {
        final MonthlyGoalCreateCommand createCommand = new MonthlyGoalCreateCommand("메인 페이지 개발 완료", "서비스 출시.");
        final Long monthlyGoalId = monthlyGoalWriteUseCase.save(dailygeUser, createCommand);

        given(this.specification)
            .filter(document(IDENTIFIER,
                MONTHLY_GOAL_AUTHORIZATION_HEADER,
                MONTHLY_GOAL_PATH_PARAMETER_SNIPPET
            ))
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .delete("/api/monthly-goals/{monthlyGoalId}", monthlyGoalId)
            .then()
            .statusCode(204)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 월간 목표를 삭제하면 204 No-Content 응답을 받는다.")
    void whenDeleteMonthlyGoalThenStatusCodeShouldBe204_Swagger() {
        final RestDocumentationFilter filter = deleteMonthlyGoalFilter(createIdentifier("MonthlyGoalDelete", 204));
        final MonthlyGoalCreateCommand createCommand = new MonthlyGoalCreateCommand("메인 페이지 개발 완료", "서비스 출시.");
        final Long monthlyGoalId = monthlyGoalWriteUseCase.save(dailygeUser, createCommand);

        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .delete("/api/monthly-goals/{monthlyGoalId}", monthlyGoalId)
            .then()
            .statusCode(204)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 파라미터를 입력하면 400 Bad Request 응답을 받는다.")
    void whenInvalidParameterThenStatusCodeShouldBe400_Swagger() {
        final RestDocumentationFilter filter = deleteMonthlyGoalErrorFilter(createIdentifier("MonthlyGoalDelete", 400));

        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .delete("/api/monthly-goals/{monthlyGoalId}", "invalid")
            .then()
            .statusCode(400)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 파라미터를 입력하면 404 Not Found 응답을 받는다.")
    void whenMonthlyGoalNotExistsThenStatusCodeShouldBe404() {
        final RestDocumentationFilter filter = deleteMonthlyGoalErrorFilter(createIdentifier("MonthlyGoalDelete", 404));

        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .delete("/api/monthly-goals/{monthlyGoalId}", 300)
            .then()
            .statusCode(404)
            .log()
            .all();
    }
}
