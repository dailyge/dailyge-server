package project.dailyge.app.test.anniversary.documentationtest;

import static io.restassured.RestAssured.given;
import static java.time.LocalDate.now;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.anniversary.application.command.AnniversaryCreateCommand;
import project.dailyge.app.core.anniversary.facade.AnniversaryFacade;
import static project.dailyge.app.test.anniversary.documentationtest.snippet.AnniversaryReadSnippet.createAnniversariesReadFilter;
import static project.dailyge.app.test.anniversary.documentationtest.snippet.AnniversarySnippet.ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.anniversary.documentationtest.snippet.AnniversarySnippet.ANNIVERSARIES_READ_RESPONSE_SNIPPET;
import static project.dailyge.app.test.anniversary.documentationtest.snippet.AnniversarySnippet.ANNIVERSARY_DATE_QUERY_PARAMS;
import static project.dailyge.app.test.anniversary.documentationtest.snippet.AnniversarySnippet.createIdentifier;

import java.time.LocalDate;

@DisplayName("[DocumentationTest] 기념일 조회 문서화 테스트")
class AnniversaryReadDocumentationTest extends DatabaseTestBase {

    @Autowired
    private AnniversaryFacade anniversaryFacade;

    @Test
    @DisplayName("[RestDocs] 기념일이 일정 기간 내에 존재하면 200 OK 응답 코드를 받는다.")
    void whenReadAnniversariesBetweenDatesThenStatusCodeShouldBe_200_OK_RestDocs() {
        final LocalDate date = now();
        final LocalDate endDate = date.plusDays(30);
        final AnniversaryCreateCommand createCommand = new AnniversaryCreateCommand("부모님 결혼 기념일", date.atTime(0, 0), false, null);
        anniversaryFacade.save(dailygeUser, createCommand);

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                ACCESS_TOKEN_COOKIE_SNIPPET,
                ANNIVERSARY_DATE_QUERY_PARAMS,
                ANNIVERSARIES_READ_RESPONSE_SNIPPET
            ))
            .param("startDate", date.toString())
            .param("endDate", endDate.toString())
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .get("/api/anniversaries")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 기념일이 일정 기간 내에 존재하면 200 OK 응답 코드를 받는다.")
    void whenReadAnniversariesBetweenDatesThenStatusCodeShouldBe_200_OK_Swagger() {
        final LocalDate date = now();
        final LocalDate endDate = date.plusDays(30);
        final AnniversaryCreateCommand createCommand = new AnniversaryCreateCommand("부모님 결혼 기념일", date.atTime(0, 0), false, null);
        anniversaryFacade.save(dailygeUser, createCommand);

        final RestDocumentationFilter filter = createAnniversariesReadFilter(createIdentifier("AnniversariesRead", 200));
        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .param("startDate", date.toString())
            .param("endDate", endDate.toString())
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .get("/api/anniversaries")
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 파라미터가 입력되면 400 Bad Request가 반환된다.")
    void whenReadAnniversariesBetweenDatesThenStatusCodeShouldBe_400_Bad_Request_Swagger() {
        final LocalDate date = now();
        final AnniversaryCreateCommand createCommand = new AnniversaryCreateCommand("부모님 결혼 기념일", date.atTime(0, 0), false, null);
        anniversaryFacade.save(dailygeUser, createCommand);

        final RestDocumentationFilter filter = createAnniversariesReadFilter(createIdentifier("AnniversariesRead", 400));
        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .get("/api/anniversaries")
            .then()
            .statusCode(400)
            .log()
            .all();
    }
}
