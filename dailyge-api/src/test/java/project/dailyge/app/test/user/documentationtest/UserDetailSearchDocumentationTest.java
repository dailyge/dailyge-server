package project.dailyge.app.test.user.documentationtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.app.core.user.presentation.request.UserRegisterRequest;
import project.dailyge.entity.user.UserJpaEntity;

import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.*;

@DisplayName("[DocumentationTest] 사용자 저장 문서화 테스트")
class UserDetailSearchDocumentationTest extends DatabaseTestBase {

    @Autowired
    private UserWriteUseCase userWriteUseCase;

    @Test
    @DisplayName("사용자가 조회 시, 200 OK 응답을 받는다.")
    void whenFindUserThenStatusCodeShouldBe200_OK() {
        final UserRegisterRequest request = new UserRegisterRequest("testName", "test@gmail.com", null);
        final UserJpaEntity saveUser = userWriteUseCase.save(request.toEntity());

        given(this.specification)
            .filter(document(IDENTIFIER,
                USER_AUTHORIZATION_HEADER,
                USER_SEARCH_PATH_PARAMETER_SNIPPET,
                USER_SEARCH_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .when()
            .get("/api/users/{userId}", saveUser.getId())
            .then()
            .statusCode(200)
            .log()
            .all();
    }
}
