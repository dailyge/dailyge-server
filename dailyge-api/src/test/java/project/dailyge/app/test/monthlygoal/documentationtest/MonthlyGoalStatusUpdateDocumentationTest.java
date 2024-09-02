package project.dailyge.app.test.monthlygoal.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.monthlygoal.application.MonthlyGoalWriteUseCase;
import project.dailyge.app.core.monthlygoal.application.command.MonthlyGoalCreateCommand;
import project.dailyge.app.core.monthlygoal.presentation.request.MonthlyGoalStatusUpdateRequest;
import static project.dailyge.app.test.monthlygoal.documentationtest.snippet.MonthlyGoalSnippet.createIdentifier;
import static project.dailyge.app.test.monthlygoal.documentationtest.snippet.MonthlyGoalUpdateSnippet.updateMonthlyGoalStatusFilter;

import java.time.LocalDate;

@DisplayName("[DocumentationTest] 월간 목표 삭제 문서화 테스트")
class MonthlyGoalStatusUpdateDocumentationTest extends DatabaseTestBase {

    @Autowired
    private MonthlyGoalWriteUseCase monthlyGoalWriteUseCase;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
    }

    @Test
    @DisplayName("[RestDocs] 월간 목표 상태를 수정하면 200 OK 응답을 받는다.")
    void whenUpdateMonthlyGoalStatusThenStatusCodeShouldBe200_RestDocs() throws JsonProcessingException {
        final MonthlyGoalCreateCommand createCommand = new MonthlyGoalCreateCommand("메인 페이지 개발 완료", "서비스 출시.", now);
        final Long monthlyGoalId = monthlyGoalWriteUseCase.save(dailygeUser, createCommand);

        final MonthlyGoalStatusUpdateRequest request = new MonthlyGoalStatusUpdateRequest(true);

        given(this.specification)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .patch("/api/monthly-goals/{monthlyGoalId}/status", monthlyGoalId)
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 월간 목표 상태를 수정하면 200 OK 응답을 받는다.")
    void whenUpdateMonthlyGoalStatusThenStatusCodeShouldBe200_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = updateMonthlyGoalStatusFilter(createIdentifier("MonthlyGoalStatus", 200));
        final MonthlyGoalCreateCommand createCommand = new MonthlyGoalCreateCommand("메인 페이지 개발 완료", "서비스 출시.", now);
        final Long monthlyGoalId = monthlyGoalWriteUseCase.save(dailygeUser, createCommand);

        final MonthlyGoalStatusUpdateRequest request = new MonthlyGoalStatusUpdateRequest(true);

        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .patch("/api/monthly-goals/{monthlyGoalId}/status", monthlyGoalId)
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 파라미터를 입력하면 400 Bad Request 응답을 받는다.")
    void whenUpdateMonthlyGoalStatusWithInvalidParameterThenStatusCodeShouldBe400_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = updateMonthlyGoalStatusFilter(createIdentifier("MonthlyGoalStatus", 200));
        final MonthlyGoalStatusUpdateRequest request = new MonthlyGoalStatusUpdateRequest(true);

        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .patch("/api/monthly-goals/{monthlyGoalId}/status", "invalidId")
            .then()
            .statusCode(400)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 존재하지 않는 월간 목표 ID를 입력하면 404 Not Found 응답을 받는다.")
    void whenUpdateMonthlyGoalStatusButNotExistsThenStatusCodeShouldBe_404() throws JsonProcessingException {
        final RestDocumentationFilter filter = updateMonthlyGoalStatusFilter(createIdentifier("MonthlyGoalStatusUpdate", 404));
        final MonthlyGoalStatusUpdateRequest request = new MonthlyGoalStatusUpdateRequest(true);

        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .patch("/api/monthly-goals/{monthlyGoalId}/status", 300)
            .then()
            .statusCode(404)
            .log()
            .all();
    }
}
