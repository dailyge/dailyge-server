package project.dailyge.app.test.user.documentationtest;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.fixture.user.UserFixture;
import project.dailyge.entity.user.UserJpaEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.USER_AUTHORIZATION_HEADER;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.USER_DELETE_PATH_PARAMETER_SNIPPET;

@DisplayName("[DocumentationTest] 유저 삭제 API 문서화 테스트")
class UserDeleteDocumentationTest extends DatabaseTestBase {

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Test
    @DisplayName("사용자를 삭제하는데 성공하면, 204 NO_CONTENT 응답을 받는다.")
    void whenDeleteUserSucceedsThenStatusCodeShouldBe204_NO_CONTENT() {
        final UserJpaEntity saveUser = userWriteUseCase.save(UserFixture.createUserJpaEntity());

        RestAssured.given(this.specification)
            .filter(document(IDENTIFIER,
                USER_AUTHORIZATION_HEADER,
                USER_DELETE_PATH_PARAMETER_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .when()
            .delete("/api/users/{userId}", saveUser.getId())
            .then()
            .statusCode(204)
            .log()
            .all();
    }
}
