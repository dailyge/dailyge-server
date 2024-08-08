package project.dailyge.app.test.user.documentationtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;

import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static project.dailyge.app.test.user.documentationtest.snippet.UserReadSnippet.createUserSearchFilter;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.USER_AUTHORIZATION_HEADER;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.USER_SEARCH_PATH_PARAMETER_SNIPPET;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.USER_SEARCH_RESPONSE_SNIPPET;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.createIdentifier;

@DisplayName("[DocumentationTest] 사용자 저장 문서화 테스트")
class UserReadDocumentationTest extends DatabaseTestBase {

    @Test
    @DisplayName("[RestDocs] User 정보 조회 시, 200 OK 응답을 받는다.")
    void whenFindUserThenStatusCodeShouldBe_200_OK_RestDocs() {
        given(this.specification)
            .filter(document(IDENTIFIER,
                USER_AUTHORIZATION_HEADER,
                USER_SEARCH_PATH_PARAMETER_SNIPPET,
                USER_SEARCH_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .when()
            .get("/api/users/{userId}", dailygeUser.getId())
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] User 정보 조회 시, 200 OK 응답을 받는다.")
    void whenFindUserThenStatusCodeShouldBe_200_OK_Swagger() {
        final RestDocumentationFilter filter = createUserSearchFilter(createIdentifier("UserSearch", 200));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .when()
            .get("/api/users/{userId}", dailygeUser.getId())
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 다른 사용자 정보를 조회하면, 403 UnAuthorized 응답을 받는다.")
    void whenFindOtherUserThenStatusCodeShouldBe_403_UnAuthorized_Swagger() {
        final RestDocumentationFilter filter = createUserSearchFilter(createIdentifier("UserSearch", 403));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, getAuthorizationHeader())
            .when()
            .get("/api/users/{userId}", Long.MAX_VALUE)
            .then()
            .statusCode(403)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 로그인 정보가 올바르지 않을 경우, 403 UnAuthorized 응답을 받는다.")
    void whenLoginInfoIsIncorrectThenStatusCodeShouldBe_403_UnAuthorized_Swagger() {
        final RestDocumentationFilter filter = createUserSearchFilter(createIdentifier("UserSearch", 403));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, "ABCD")
            .when()
            .get("/api/users/{userId}", Long.MAX_VALUE)
            .then()
            .statusCode(403)
            .log()
            .all();
    }
}
