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
import project.dailyge.app.core.monthlygoal.presentation.request.MonthlyGoalCreateRequest;
import static project.dailyge.app.test.monthlygoal.documentationtest.snippet.MonthlyGoalSearchSnippet.createMonthlyGoalSearchFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.createIdentifier;

import java.time.LocalDate;

@DisplayName("[DocumentationTest] 월간 목표 조회 문서화 테스트")
class MonthlyGoalReadDocumentationTest extends DatabaseTestBase {

    private MonthlyGoalCreateRequest request;

    @Autowired
    private MonthlyGoalWriteUseCase monthlyGoalWriteUseCase;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
        request = new MonthlyGoalCreateRequest("메인 페이지 개발 완료", "서비스 출시.", now);
        monthlyGoalWriteUseCase.save(dailygeUser, request.toCommand());
    }

    @Test
    @DisplayName("[RestDocs] 월간 목표를 조회하면 200 OK 응답을 받는다.")
    void whenSearchMonthlyGoalWithCursorThenStatusCodeShouldBe_200_OK_RestDocs() throws JsonProcessingException {
        given(this.specification)
            .relaxedHTTPSValidation()
            .body(objectMapper.writeValueAsString(request))
            .param("year", now.getYear())
            .param("month", now.getMonthValue())
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .get("/api/monthly-goals")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 월간 목표를 조회하면 200 OK 응답을 받는다.")
    void whenSearchMonthlyGoalWithCursorThenStatusCodeShouldBe_200_OK_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = createMonthlyGoalSearchFilter(createIdentifier("MonthlyGoalSearch", 200));
        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .body(objectMapper.writeValueAsString(request))
            .param("year", now.getYear())
            .param("month", now.getMonthValue())
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .get("/api/monthly-goals")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 월간 목표를 조회할 때, 올바르지 않은 파라미터를 넣으면 400 Bad Request 응답을 받는다.")
    void whenSearchMonthlyGoalWithCursorWithInvalidParameterThenStatusCodeShouldBe_400_Bad_Request_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = createMonthlyGoalSearchFilter(createIdentifier("MonthlyGoalSearch", 400));
        given(this.specification)
            .filter(filter)
            .relaxedHTTPSValidation()
            .body(objectMapper.writeValueAsString(request))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .get("/api/monthly-goals")
            .then()
            .statusCode(400)
            .log()
            .all();
    }
}
