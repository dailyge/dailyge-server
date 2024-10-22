package project.dailyge.app.test.weeklygoal.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.weeklygoal.application.WeeklyGoalWriteService;
import project.dailyge.app.core.weeklygoal.presentation.request.WeeklyGoalCreateRequest;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalSearchSnippet.createWeeklyGoalSearchFilter;
import static project.dailyge.app.test.weeklygoal.documentationtest.snippet.WeeklyGoalSnippet.createIdentifier;

@DisplayName("[DocumentationTest] 주간 목표 조회 문서화 테스트")
class WeeklyGoalReadDocumentationTest extends DatabaseTestBase {

    private static final String DEFAULT_TITLE = "주간 목표 수정 API 개발";
    private static final String DEFAULT_CONTENT = "원격 저장소 올리기 전에 셀프 리뷰";

    private WeeklyGoalCreateRequest request;

    @Autowired
    private WeeklyGoalWriteService weeklyGoalWriteService;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
        request = new WeeklyGoalCreateRequest(DEFAULT_TITLE, DEFAULT_CONTENT, now);
        weeklyGoalWriteService.save(dailygeUser, request.toCommand());
    }

    @Test
    @DisplayName("[RestDocs] 주간 목표를 조회하면 200 OK 응답을 받는다.")
    void whenSearchWeeklyGoalWithCursorThenStatusCodeShouldBe_200_OK_RestDocs() throws JsonProcessingException {
        given(this.specification)
            .relaxedHTTPSValidation()
            .param("weekStartDate", now.with(DayOfWeek.MONDAY).toString())
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .get("/api/weekly-goals")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 주간 목표를 조회하면 200 OK 응답을 받는다.")
    void whenSearchWeeklyGoalWithCursorThenStatusCodeShouldBe_200_OK_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = createWeeklyGoalSearchFilter(createIdentifier("WeeklyGoalSearch", 200));
        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .param("weekStartDate", now.with(DayOfWeek.MONDAY).toString())
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .get("/api/weekly-goals")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 주간 목표를 조회할 때, 올바르지 않은 파라미터를 넣으면 400 Bad Request 응답을 받는다.")
    void whenSearchWeeklyGoalWithCursorWithInvalidParameterThenStatusCodeShouldBe_400_Bad_Request_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = createWeeklyGoalSearchFilter(createIdentifier("WeeklyGoalSearch", 400));
        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .get("/api/weekly-goals")
            .then()
            .statusCode(400)
            .log()
            .all();
    }
}
