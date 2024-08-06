package project.dailyge.app.test.user.documentationtest;

import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import project.dailyge.app.common.DatabaseTestBase;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.USER_AUTHORIZATION_HEADER;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.USER_SEARCH_PATH_PARAMETER_SNIPPET;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.USER_SEARCH_RESPONSE_SNIPPET;

@DisplayName("[DocumentationTest] 사용자 저장 문서화 테스트")
class UserDetailSearchDocumentationTest extends DatabaseTestBase {

    @Test
    @DisplayName("사용자가 조회 시, 200 OK 응답을 받는다.")
    void whenFindUserThenStatusCodeShouldBe200_OK() {
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
}
