package project.dailyge.app.test.retrospect.documentationtest;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.retrospect.application.RetrospectWriteService;
import project.dailyge.app.core.retrospect.presentation.request.RetrospectCreateRequest;
import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static project.dailyge.app.test.retrospect.documentationtest.snippet.RetrospectDeleteSnippet.createDeleteRetrospectFilter;
import static project.dailyge.app.test.retrospect.documentationtest.snippet.RetrospectSnippet.ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.retrospect.documentationtest.snippet.RetrospectSnippet.RETROSPECT_PATH_PARAMETER_SNIPPET;
import static project.dailyge.app.test.retrospect.documentationtest.snippet.RetrospectSnippet.createIdentifier;

@DisplayName("[DocumentationTest] 회고 삭제 문서화 테스트")
class RetrospectDeleteDocumentationTest extends DatabaseTestBase {

    private Long retrospectId;

    @Autowired
    private RetrospectWriteService retrospectWriteService;

    @BeforeEach
    void setUp() {
        final RetrospectCreateRequest createRequest = new RetrospectCreateRequest("회고 제목", "회고 내용", LocalDate.now(), false);
        retrospectId = retrospectWriteService.save(dailygeUser, createRequest.toCommand());
    }

    @Test
    @DisplayName("[RestDocs] 회고를 삭제하면 204 응답 코드를 받는다.")
    void whenDeleteRetrospectThenStatusCodeShouldBe_204_RestDocs() {
        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                ACCESS_TOKEN_COOKIE_SNIPPET,
                RETROSPECT_PATH_PARAMETER_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .delete("/api/retrospects/{retrospectId}", retrospectId)
            .then()
            .statusCode(204);
    }

    @Test
    @DisplayName("[Swagger] 회고를 삭제하면 204 응답 코드를 받는다.")
    void whenDeleteRetrospectThenStatusCodeShouldBe_200_Swagger() {
        final RestDocumentationFilter filter = createDeleteRetrospectFilter(createIdentifier("RetrospectDelete", 204));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .delete("/api/retrospects/{retrospectId}", retrospectId)
            .then()
            .statusCode(204);
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 파라미터를 넘기면 400 Bad Request 응답을 받는다.")
    void whenDeleteRetrospectWithInvalidParameterThenStatusCodeShouldBe_400_Swagger() {
        final RestDocumentationFilter filter = createDeleteRetrospectFilter(createIdentifier("RetrospectDelete", 400));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .delete("/api/retrospects/{retrospectId}", "invalidId")
            .then()
            .statusCode(400);
    }
}
