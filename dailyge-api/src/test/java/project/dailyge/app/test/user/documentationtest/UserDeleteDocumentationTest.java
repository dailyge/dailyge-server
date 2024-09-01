package project.dailyge.app.test.user.documentationtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static project.dailyge.app.test.user.documentationtest.snippet.UserDeleteSnippet.createUserDeleteFilter;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.USER_ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.USER_DELETE_PATH_PARAMETER_SNIPPET;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.USER_DELETE_RESPONSE_COOKIE_SNIPPET;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.createIdentifier;

@DisplayName("[DocumentationTest] 유저 삭제 API 문서화 테스트")
class UserDeleteDocumentationTest extends DatabaseTestBase {

    private static final String ACCESS_TOKEN = "Access-Token";
    private static final String REFRESH_TOKEN = "Refresh-Token";

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Test
    @DisplayName("[RestDocs] 사용자를 삭제하면, 204 NO_CONTENT 응답을 받는다.")
    void whenUserDeleteThenStatusCodeShouldBe_204_NoContent_RestDocs() {
        given(this.specification)
            .filter(document(IDENTIFIER,
                USER_ACCESS_TOKEN_COOKIE_SNIPPET,
                USER_DELETE_PATH_PARAMETER_SNIPPET,
                USER_DELETE_RESPONSE_COOKIE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .delete("/api/users/{userId}", newUser.getId())
            .then()
            .cookie(ACCESS_TOKEN)
            .cookie(REFRESH_TOKEN)
            .statusCode(204)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 사용자를 삭제하면, 204 NO_CONTENT 응답을 받는다.")
    void whenUserDeleteThenStatusCodeShouldBe_204_NoContent_Swagger() {
        final RestDocumentationFilter filter = createUserDeleteFilter(createIdentifier("UserDelete", 204));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .delete("/api/users/{userId}", newUser.getId())
            .then()
            .cookie(ACCESS_TOKEN)
            .cookie(REFRESH_TOKEN)
            .statusCode(204)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 이미 삭제되었다면, 404 NOT FOUND 응답을 받는다.")
    void whenUserAlreadyDeletedThenStatusCodeShouldBe_404_NotFound_Swagger() {
        final RestDocumentationFilter filter = createUserDeleteFilter(createIdentifier("UserDelete", 404));
        userWriteUseCase.delete(newUser.getId());

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .delete("/api/users/{userId}", newUser.getId())
            .then()
            .statusCode(404)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 로그인 정보가 올바르지 않은 경우, 403 UnAuthorized 응답을 받는다.")
    void whenLoginInfoIsIncorrectThenStatusCodeShouldBe_403_UnAuthorized_Swagger() {
        final RestDocumentationFilter filter = createUserDeleteFilter(createIdentifier("UserDelete", 403));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(ACCESS_TOKEN, "ABCD")
            .when()
            .delete("/api/users/{userId}", newUser.getId())
            .then()
            .statusCode(403)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 로그인 정보가 일치하지 않은 경우, 403 UnAuthorized 응답을 받는다.")
    void whenLoginInfoNotMatchThenStatusCodeShouldBe_403_UnAuthorized_Swagger() {
        final RestDocumentationFilter filter = createUserDeleteFilter(createIdentifier("UserDelete", 403));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .delete("/api/users/{userId}", Long.MAX_VALUE)
            .then()
            .statusCode(403)
            .log()
            .all();
    }
}
