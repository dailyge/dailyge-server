package project.dailyge.app.test.user.documentationtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static project.dailyge.app.test.user.documentationtest.snippet.LogoutSnippet.createLogoutFilter;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.LOGOUT_RESPONSE_COOKIE_SNIPPET;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.createIdentifier;

@DisplayName("[DocumentationTest] 로그아웃 API 문서화 테스트")
class LogoutDocumentationTest extends DatabaseTestBase {

    private static final String ACCESS_TOKEN = "Access-Token";
    private static final String REFRESH_TOKEN = "Refresh-Token";

    @Test
    @DisplayName("[RestDocs] 로그아웃을 하면, 200 OK 응답을 받는다.")
    void whenLogoutThenStatusCodeShouldBe_200_OK_RestDocs() {
        given(this.specification)
            .filter(document(IDENTIFIER,
                LOGOUT_RESPONSE_COOKIE_SNIPPET
            ))
            .cookie(getAccessTokenCookie())
            .contentType(APPLICATION_JSON_VALUE)
            .when()
            .post("/api/logout")
            .then()
            .cookie(ACCESS_TOKEN)
            .cookie(REFRESH_TOKEN)
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 로그아웃을 하면, 200 OK 응답을 받는다.")
    void whenLogoutThenStatusCodeShouldBe_200_OK_Swagger() {
        final RestDocumentationFilter filter = createLogoutFilter(createIdentifier("Logout", 200));

        given(this.specification)
            .filter(filter)
            .cookie(getAccessTokenCookie())
            .contentType(APPLICATION_JSON_VALUE)
            .when()
            .post("/api/logout")
            .then()
            .cookie(ACCESS_TOKEN)
            .cookie(REFRESH_TOKEN)
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 로그인 정보가 올바르지 않을 경우, 403 UnAuthorized 응답을 받는다.")
    void whenLoginInfoIsIncorrectThenStatusCodeShouldBe_403_UnAuthorized_Swagger() {
        final RestDocumentationFilter filter = createLogoutFilter(createIdentifier("Logout", 403));

        given(this.specification)
            .filter(filter)
            .cookie("Access-Token", "ABCD")
            .contentType(APPLICATION_JSON_VALUE)
            .when()
            .post("/api/logout")
            .then()
            .statusCode(403)
            .log()
            .all();
    }
}
