package project.dailyge.app.test.weeklygoal.documentationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.weeklygoal.application.WeeklyGoalWriteService;
import project.dailyge.app.core.weeklygoal.application.command.WeeklyGoalCreateCommand;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalDeleteSnippet.deleteWeeklyGoalErrorFilter;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalDeleteSnippet.deleteWeeklyGoalFilter;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalSnippet.WEEKLY_GOAL_ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalSnippet.WEEKLY_GOAL_PATH_PARAMETER_SNIPPET;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalSnippet.createIdentifier;
import static project.dailyge.entity.user.Role.NORMAL;

@DisplayName("[DocumentationTest] 주간 목표 삭제 문서화 테스트")
class WeeklyGoalDeleteDocumentationTest extends DatabaseTestBase {

    private static final String DEFAULT_TITLE = "주간 목표 수정 API 개발";
    private static final String DEFAULT_CONTENT = "원격 저장소 올리기 전에 셀프 리뷰";

    @Autowired
    private WeeklyGoalWriteService weeklyGoalWriteService;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
    }

    @Test
    @DisplayName("[RestDocs] 주간 목표를 삭제하면 204 No-Content 응답을 받는다.")
    void whenDeleteWeeklyGoalThenStatusCodeShouldBe204_RestDocs() {
        final WeeklyGoalCreateCommand createCommand = new WeeklyGoalCreateCommand(DEFAULT_TITLE, DEFAULT_CONTENT, now);
        final Long weeklyGoalId = weeklyGoalWriteService.save(dailygeUser, createCommand);

        given(this.specification)
            .filter(document(IDENTIFIER,
                WEEKLY_GOAL_ACCESS_TOKEN_COOKIE_SNIPPET,
                WEEKLY_GOAL_PATH_PARAMETER_SNIPPET
            ))
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .delete("/api/weekly-goals/{weeklyGoalId}", weeklyGoalId)
            .then()
            .statusCode(204)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 주간 목표를 삭제하면 204 No-Content 응답을 받는다.")
    void whenDeleteWeeklyGoalThenStatusCodeShouldBe204_Swagger() {
        final RestDocumentationFilter filter = deleteWeeklyGoalFilter(createIdentifier("WeeklyGoalDelete", 204));
        final WeeklyGoalCreateCommand createCommand = new WeeklyGoalCreateCommand(DEFAULT_TITLE, DEFAULT_CONTENT, now);
        final Long weeklyGoalId = weeklyGoalWriteService.save(dailygeUser, createCommand);

        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .delete("/api/weekly-goals/{weeklyGoalId}", weeklyGoalId)
            .then()
            .statusCode(204)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 파라미터를 입력하면 400 Bad Request 응답을 받는다.")
    void whenInvalidParameterThenStatusCodeShouldBe400_Swagger() {
        final RestDocumentationFilter filter = deleteWeeklyGoalErrorFilter(createIdentifier("WeeklyGoalDelete", 400));

        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .delete("/api/weekly-goals/{weeklyGoalId}", "invalid")
            .then()
            .statusCode(400)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 파라미터를 입력하면 404 Not Found 응답을 받는다.")
    void whenWeeklyGoalNotExistsThenStatusCodeShouldBe_404() {
        final RestDocumentationFilter filter = deleteWeeklyGoalErrorFilter(createIdentifier("WeeklyGoalDelete", 404));

        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .delete("/api/weekly-goals/{weeklyGoalId}", 300)
            .then()
            .statusCode(404)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 권한이 없는 사용자가 주간 목표 상태를 삭제하면 403 Forbidden 응답을 받는다.")
    void whenDeleteWeeklyGoalByUnAuthorizedUserThenStatusCodeShouldBe_403() {
        final WeeklyGoalCreateCommand createCommand = new WeeklyGoalCreateCommand(DEFAULT_TITLE, DEFAULT_CONTENT, now);
        final Long weeklyGoalId = weeklyGoalWriteService.save(new DailygeUser(Long.MAX_VALUE, NORMAL), createCommand);
        final RestDocumentationFilter filter = deleteWeeklyGoalErrorFilter(createIdentifier("WeeklyGoalDelete", 403));

        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .delete("/api/weekly-goals/{weeklyGoalId}", weeklyGoalId)
            .then()
            .statusCode(403)
            .log()
            .all();
    }
}
