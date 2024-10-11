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
import project.dailyge.app.core.weeklygoal.request.WeeklyGoalStatusUpdateRequest;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static project.dailyge.app.test.monthlygoal.documentationtest.snippet.MonthlyGoalSnippet.createIdentifier;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalUpdateSnippet.updateWeeklyGoalStatusErrorFilter;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalUpdateSnippet.updateWeeklyGoalStatusFilter;
import static project.dailyge.entity.user.Role.NORMAL;

@DisplayName("[DocumentationTest] 주간 목표 달성 상태 수정 문서화 테스트")
class WeeklyGoalDoneUpdateDocumentationTest extends DatabaseTestBase {

    private static final String DEFAULT_TITLE = "주간 목표 수정 API 개발";
    private static final String DEFAULT_CONTENT = "원격 저장소 올리기 전에 셀프 리뷰";

    @Autowired
    private WeeklyGoalWriteService weeklyGoalWriteService;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
    }

    @Test
    @DisplayName("[RestDocs] 주간 목표 달성 상태를 수정하면 200 OK 응답을 받는다.")
    void whenUpdateWeeklyGoalStatusThenStatusCodeShouldBe200_RestDocs() throws JsonProcessingException {
        final WeeklyGoalCreateCommand createCommand = new WeeklyGoalCreateCommand(DEFAULT_TITLE, DEFAULT_CONTENT, now);
        final Long weeklyGoalId = weeklyGoalWriteService.save(dailygeUser, createCommand);
        final WeeklyGoalStatusUpdateRequest request = new WeeklyGoalStatusUpdateRequest(true);

        given(this.specification)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .patch("/api/weekly-goals/{weeklyGoalId}/status", weeklyGoalId)
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 주간 목표 달성 상태를 수정하면 200 OK 응답을 받는다.")
    void whenUpdateWeeklyGoalStatusThenStatusCodeShouldBe200_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = updateWeeklyGoalStatusFilter(createIdentifier("WeeklyGoalStatusUpdate", 200));
        final WeeklyGoalCreateCommand createCommand = new WeeklyGoalCreateCommand(DEFAULT_TITLE, DEFAULT_CONTENT, now);
        final Long weeklyGoalId = weeklyGoalWriteService.save(dailygeUser, createCommand);
        final WeeklyGoalStatusUpdateRequest request = new WeeklyGoalStatusUpdateRequest(true);

        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .patch("/api/weekly-goals/{weeklyGoalId}/status", weeklyGoalId)
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 파라미터를 입력하면 400 Bad Request 응답을 받는다.")
    void whenUpdateWeeklyGoalStatusWithInvalidParameterThenStatusCodeShouldBe400_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = updateWeeklyGoalStatusErrorFilter(createIdentifier("WeeklyGoalStatusUpdate", 200));
        final WeeklyGoalStatusUpdateRequest request = new WeeklyGoalStatusUpdateRequest(true);

        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .patch("/api/weekly-goals/{weeklyGoalId}/status", "invalidId")
            .then()
            .statusCode(400)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 존재하지 않는 주간 목표 ID를 입력하면 404 Not Found 응답을 받는다.")
    void whenUpdateWeeklyGoalStatusButNotExistsThenStatusCodeShouldBe_404() throws JsonProcessingException {
        final RestDocumentationFilter filter = updateWeeklyGoalStatusErrorFilter(createIdentifier("WeeklyGoalStatusUpdate", 404));
        final WeeklyGoalStatusUpdateRequest request = new WeeklyGoalStatusUpdateRequest(true);

        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .patch("/api/weekly-goals/{weeklyGoalId}/status", 300)
            .then()
            .statusCode(404)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 권한이 없는 사용자가 주간 목표 상태를 수정하면 403 Forbidden 응답을 받는다.")
    void whenUpdateWeeklyGoalStatusByUnAuthorizedUserThenStatusCodeShouldBe_403() throws JsonProcessingException {
        final WeeklyGoalCreateCommand createCommand = new WeeklyGoalCreateCommand(DEFAULT_TITLE, DEFAULT_CONTENT, now);
        final Long weeklyGoalId = weeklyGoalWriteService.save(new DailygeUser(Long.MAX_VALUE, NORMAL), createCommand);
        final RestDocumentationFilter filter = updateWeeklyGoalStatusErrorFilter(createIdentifier("WeeklyGoalStatusUpdate", 403));
        final WeeklyGoalStatusUpdateRequest request = new WeeklyGoalStatusUpdateRequest(true);

        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .patch("/api/weekly-goals/{weeklyGoalId}/status", weeklyGoalId)
            .then()
            .statusCode(403)
            .log()
            .all();
    }
}
