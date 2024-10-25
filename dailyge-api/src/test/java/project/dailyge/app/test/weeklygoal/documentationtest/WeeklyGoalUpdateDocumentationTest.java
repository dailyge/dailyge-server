package project.dailyge.app.test.weeklygoal.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.weeklygoal.application.WeeklyGoalWriteService;
import project.dailyge.app.core.weeklygoal.application.command.WeeklyGoalCreateCommand;
import project.dailyge.app.core.weeklygoal.application.command.WeeklyGoalUpdateCommand;
import project.dailyge.app.core.weeklygoal.presentation.request.WeeklyGoalUpdateRequest;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalSnippet.WEEKLY_GOAL_ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalSnippet.WEEKLY_GOAL_PATH_PARAMETER_SNIPPET;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalSnippet.WEEKLY_GOAL_UPDATE_REQUEST_SNIPPET;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalSnippet.WEEKLY_GOAL_UPDATE_RESPONSE_SNIPPET;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalSnippet.createIdentifier;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalUpdateSnippet.updateWeeklyGoalErrorFilter;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalUpdateSnippet.updateWeeklyGoalFilter;
import static project.dailyge.entity.user.Role.NORMAL;

@DisplayName("[DocumentationTest] 주간 목표 수정 문서화 테스트")
class WeeklyGoalUpdateDocumentationTest extends DatabaseTestBase {

    private static final String DEFAULT_TITLE = "주간 목표 수정 API 개발";
    private static final String DEFAULT_CONTENT = "원격 저장소 올리기 전에 셀프 리뷰";
    private static final String UPDATE_TITLE = "주간 목표 수정 API 개발 수정";
    private static final String UPDATE_CONTENT = "엔티티 기본 생성자 수정";

    @Autowired
    private WeeklyGoalWriteService weeklyGoalWriteService;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
    }

    @Test
    @DisplayName("[RestDocs] 주간 목표를 수정하면 200 OK 응답을 받는다.")
    void whenUpdateWeeklyGoalThenStatusCodeShouldBe200_RestDocs() throws JsonProcessingException {
        final WeeklyGoalCreateCommand createCommand = new WeeklyGoalCreateCommand(DEFAULT_TITLE, DEFAULT_CONTENT, now);
        final Long weeklyGoalId = weeklyGoalWriteService.save(dailygeUser, createCommand);
        final WeeklyGoalUpdateCommand request = new WeeklyGoalUpdateCommand(UPDATE_TITLE, UPDATE_CONTENT);

        given(this.specification)
            .filter(document(IDENTIFIER,
                WEEKLY_GOAL_ACCESS_TOKEN_COOKIE_SNIPPET,
                WEEKLY_GOAL_PATH_PARAMETER_SNIPPET,
                WEEKLY_GOAL_UPDATE_REQUEST_SNIPPET,
                WEEKLY_GOAL_UPDATE_RESPONSE_SNIPPET
            ))
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .put("/api/weekly-goals/{weeklyGoalId}", weeklyGoalId)
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 주간 목표를 수정하면 200 OK 응답을 받는다.")
    void whenUpdateWeeklyGoalThenStatusCodeShouldBe200_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = updateWeeklyGoalFilter(createIdentifier("WeeklyGoalUpdate", 200));
        final WeeklyGoalCreateCommand createCommand = new WeeklyGoalCreateCommand(DEFAULT_TITLE, DEFAULT_CONTENT, now);
        final Long weeklyGoalId = weeklyGoalWriteService.save(dailygeUser, createCommand);
        final WeeklyGoalUpdateCommand request = new WeeklyGoalUpdateCommand(UPDATE_TITLE, UPDATE_CONTENT);

        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .put("/api/weekly-goals/{weeklyGoalId}", weeklyGoalId)
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 파라미터를 입력하면 400 Bad Request 응답을 받는다.")
    void whenUpdateWeeklyGoalButInvalidParameterThenStatusCodeShouldBe400_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = updateWeeklyGoalErrorFilter(createIdentifier("WeeklyGoalUpdate", 400));
        final WeeklyGoalUpdateCommand request = new WeeklyGoalUpdateCommand(UPDATE_TITLE, UPDATE_CONTENT);

        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .put("/api/weekly-goals/{weeklyGoalId}", "invalidId")
            .then()
            .statusCode(400)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 존재하지 않는 주간 목표 ID를 입력하면 404 Not Found 응답을 받는다.")
    void whenUpdateWeeklyGoalButNotExistsThenStatusCodeShouldBe_404() throws JsonProcessingException {
        final RestDocumentationFilter filter = updateWeeklyGoalErrorFilter(createIdentifier("WeeklyGoalUpdate", 404));
        final WeeklyGoalUpdateRequest request = new WeeklyGoalUpdateRequest(UPDATE_TITLE, UPDATE_CONTENT);

        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .put("/api/weekly-goals/{weeklyGoalId}", 300)
            .then()
            .statusCode(404)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 권한이 없는 사용자가 주간 목표를 수정하면 403 Forbidden 응답을 받는다.")
    void whenUpdateWeeklyGoalByUnAuthorizedUserThenStatusCodeShouldBe_403() throws JsonProcessingException {
        final WeeklyGoalCreateCommand createCommand = new WeeklyGoalCreateCommand(DEFAULT_TITLE, DEFAULT_CONTENT, now);
        final Long weeklyGoalId = weeklyGoalWriteService.save(new DailygeUser(Long.MAX_VALUE, NORMAL), createCommand);
        final WeeklyGoalUpdateCommand request = new WeeklyGoalUpdateCommand(UPDATE_TITLE, UPDATE_CONTENT);
        final RestDocumentationFilter filter = updateWeeklyGoalErrorFilter(createIdentifier("WeeklyGoalUpdate", 403));

        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .put("/api/weekly-goals/{weeklyGoalId}", weeklyGoalId)
            .then()
            .statusCode(403)
            .log()
            .all();
    }
}
