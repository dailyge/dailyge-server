package project.dailyge.app.test.user.documentationtest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DocumentationTestBase;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.fixture.user.UserFixture;
import project.dailyge.domain.user.UserJpaEntity;

import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.*;

@DisplayName("[DocumentationTest] 유저 삭제 API 문서화 테스트")
public class UserDeleteDocumentationTest extends DocumentationTestBase {

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Test
    @DisplayName("사용자 삭제 테스트")
    void whenDeleteExistsUserThenShould200_Ok() {
        UserJpaEntity saveUser = userWriteUseCase.save(UserFixture.createUserJpaEntity());

        RestAssured.given(this.specification)
            .filter(document(IDENTIFIER,
                    USER_AUTHORIZATION_HEADER,
                    USER_DELETE_PATH_PARAMETER_SNIPPET,
                    USER_DELETE_RESPONSE_SNIPPET
            ))
            .contentType(ContentType.JSON)
            .header(AUTHORIZATION, "Token")
            .header(USER_ID_KEY, saveUser.getId())
            .when()
            .delete("/api/users/{userId}", saveUser.getId())
            .then()
            .statusCode(200)
            .log()
            .all();
    }
}
