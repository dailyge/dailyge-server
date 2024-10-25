package project.dailyge.app.test.anniversary.documentationtest;

import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.anniversary.facade.AnniversaryFacade;
import project.dailyge.app.core.anniversary.presentation.request.AnniversaryCreateRequest;
import static project.dailyge.app.test.anniversary.documentationtest.snippet.AnniversaryDeleteSnippet.createDeleteAnniversaryFilter;
import static project.dailyge.app.test.anniversary.documentationtest.snippet.AnniversarySnippet.ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.anniversary.documentationtest.snippet.AnniversarySnippet.ANNIVERSARY_PATH_PARAMETER_SNIPPET;
import static project.dailyge.app.test.anniversary.documentationtest.snippet.AnniversarySnippet.createIdentifier;

import java.time.LocalDate;

@DisplayName("[DocumentationTest] 기념일 삭제 문서화 테스트")
class AnniversaryDeleteDocumentationTest extends DatabaseTestBase {

    @Autowired
    private AnniversaryFacade anniversaryFacade;

    @Test
    @DisplayName("[RestDocs] 기념일을 삭제하면 204 응답 코드를 받는다.")
    void whenDeleteAnniversaryThenStatusCodeShouldBe_No_Content_204_RestDocs() {
        final LocalDate date = LocalDate.now();
        final AnniversaryCreateRequest request = new AnniversaryCreateRequest("부모님 결혼 기념일", date, false, null);
        final Long newAnniversaryId = anniversaryFacade.save(dailygeUser, request.toCommand());

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                ACCESS_TOKEN_COOKIE_SNIPPET,
                ANNIVERSARY_PATH_PARAMETER_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .delete("/api/anniversaries/{anniversaryId}", newAnniversaryId)
            .then()
            .statusCode(204);
    }

    @Test
    @DisplayName("[Swagger] 기념일을 삭제하면 204 응답 코드를 받는다.")
    void whenDeleteAnniversaryThenStatusCodeShouldBe_No_Content_204_Swagger() {
        final LocalDate date = LocalDate.now();
        final AnniversaryCreateRequest request = new AnniversaryCreateRequest("부모님 결혼 기념일", date, false, null);
        final Long newAnniversaryId = anniversaryFacade.save(dailygeUser, request.toCommand());
        final RestDocumentationFilter filter = createDeleteAnniversaryFilter(createIdentifier("AnniversaryDelete", 204));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .delete("/api/anniversaries/{anniversaryId}", newAnniversaryId)
            .then()
            .statusCode(204);
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 파라미터를 입력하면 400 Bad Request를 반환한다.")
    void whenDeleteAnniversaryWithInvalidIdThenStatusCodeShouldBe_400_Bad_Request_Swagger() {
        final RestDocumentationFilter filter = createDeleteAnniversaryFilter(createIdentifier("AnniversaryDelete", 400));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .delete("/api/anniversaries/{anniversaryId}", "invalidId")
            .then()
            .statusCode(400);
    }
}
