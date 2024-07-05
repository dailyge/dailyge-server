package project.dailyge.app.test.user.documentationtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.user.application.UserWriteUseCase;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static project.dailyge.app.fixture.user.UserFixture.createUserJpaEntity;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.LOGOUT_RESPONSE_COOKIE_SNIPPET;

@DisplayName("[DocumentationTest] 로그아웃 API 문서화 테스트")
class LogoutDocumentationTest extends DatabaseTestBase {

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Test
    @DisplayName("로그아웃을 하면, 200 OK 응답을 받는다.")
    void whenLogoutThenStatusCodeShouldBe200_OK() {
        userWriteUseCase.save(createUserJpaEntity());

        given(this.specification)
            .filter(document(IDENTIFIER,
                LOGOUT_RESPONSE_COOKIE_SNIPPET
            ))
            .header(AUTHORIZATION, getAuthorizationHeader())
            .contentType(APPLICATION_JSON_VALUE)
            .when()
            .post("/api/logout")
            .then()
            .header(
                "set-Cookie",
                containsString("Refresh-Token=; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT")
            )
            .statusCode(200)
            .log()
            .all();
    }
}
