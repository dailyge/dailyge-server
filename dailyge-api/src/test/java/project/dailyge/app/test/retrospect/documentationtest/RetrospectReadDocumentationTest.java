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
import static project.dailyge.app.test.retrospect.documentationtest.snippet.RetrospectReadSnippet.createReadRetrospectFilter;
import static project.dailyge.app.test.retrospect.documentationtest.snippet.RetrospectSnippet.ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.retrospect.documentationtest.snippet.RetrospectSnippet.RETROSPECT_PAGING_PATH_PARAMETER_SNIPPET;
import static project.dailyge.app.test.retrospect.documentationtest.snippet.RetrospectSnippet.RETROSPECT_READ_RESPONSE_SNIPPET;
import static project.dailyge.app.test.retrospect.documentationtest.snippet.RetrospectSnippet.createIdentifier;

@DisplayName("[DocumentationTest] 회고 조회 문서화 테스트")
class RetrospectReadDocumentationTest extends DatabaseTestBase {

    @Autowired
    private RetrospectWriteService retrospectWriteService;

    @BeforeEach
    void setUp() {
        final RetrospectCreateRequest createRequest = new RetrospectCreateRequest("회고 제목", "회고 내용", LocalDate.now(), false);
        retrospectWriteService.save(dailygeUser, createRequest.toCommand());
    }

    @Test
    @DisplayName("[RestDocs] 회고를 조회하면 200 응답 코드를 받는다.")
    void whenReadRetrospectThenStatusCodeShouldBe_200_RestDocs() {
        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                ACCESS_TOKEN_COOKIE_SNIPPET,
                RETROSPECT_PAGING_PATH_PARAMETER_SNIPPET,
                RETROSPECT_READ_RESPONSE_SNIPPET
            ))
            .param("page", 1)
            .param("limit", 10)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .get("/api/retrospects")
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("[Swagger] 회고를 조회하면 200 응답 코드를 받는다.")
    void whenReadRetrospectThenStatusCodeShouldBe_200_Swagger() {
        final RestDocumentationFilter filter = createReadRetrospectFilter(createIdentifier("RetrospectRead", 200));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .param("page", 1)
            .param("limit", 10)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .get("/api/retrospects")
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("[Swagger] 로그인 정보가 올바르지 않은 경우, 403 UnAuthorized 응답코드를 받는다.")
    void whenUnAuthorizedUserThenStatusCodeShouldBe_403_Swagger() {
        final RestDocumentationFilter filter = createReadRetrospectFilter(createIdentifier("RetrospectRead", 403));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .param("page", 1)
            .param("limit", 10)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie("invalid-token")
            .when()
            .get("/api/retrospects")
            .then()
            .statusCode(403);
    }
}
