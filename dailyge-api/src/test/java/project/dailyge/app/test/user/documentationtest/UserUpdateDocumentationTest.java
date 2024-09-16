package project.dailyge.app.test.user.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.user.presentation.request.UserUpdateRequest;
import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.USER_ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.USER_UPDATE_PATH_PARAMETER_SNIPPET;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.USER_UPDATE_REQUEST_FIELD_SNIPPET;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.USER_UPDATE_RESPONSE_FIELD_SNIPPET;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.createIdentifier;
import static project.dailyge.app.test.user.documentationtest.snippet.UserUpdateSnippet.createUserUpdateFilter;

@DisplayName("[DocumentationTest] 유저 수정 API 문서화 테스트")
class UserUpdateDocumentationTest extends DatabaseTestBase {

    private static final String ACCESS_TOKEN = "Access-Token";
    private static final String REFRESH_TOKEN = "Refresh-Token";
    private UserUpdateRequest request;

    @BeforeEach
    void setUp() {
        request = new UserUpdateRequest("Dailyge");
    }

    @Test
    @DisplayName("[RestDocs] 사용자를 수정하면, 200 OK 응답을 받는다.")
    void whenUserUpdateThenStatusCodeShouldBe_200_RestDocs() throws JsonProcessingException {
        given(this.specification)
            .filter(document(IDENTIFIER,
                USER_ACCESS_TOKEN_COOKIE_SNIPPET,
                USER_UPDATE_REQUEST_FIELD_SNIPPET,
                USER_UPDATE_PATH_PARAMETER_SNIPPET,
                USER_UPDATE_RESPONSE_FIELD_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .body(objectMapper.writeValueAsString(request))
            .patch("/api/users/{userId}", newUser.getId())
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 사용자를 수정하면, 200 OK 응답을 받는다.")
    void whenUserUpdateThenStatusCodeShouldBe_200_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = createUserUpdateFilter(createIdentifier("UserUpdate", 200));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .body(objectMapper.writeValueAsString(request))
            .patch("/api/users/{userId}", newUser.getId())
            .then()
            .statusCode(200)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 수정 시 닉네임이 비어있으면, 400 Bad Request 응답을 받는다.")
    void whenNicknameIsEmptyThenStatusCodeShouldBe_400_Swagger() throws JsonProcessingException {
        final UserUpdateRequest requestWithEmptyNickname = new UserUpdateRequest("");
        final RestDocumentationFilter filter = createUserUpdateFilter(createIdentifier("UserUpdate", 400));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .body(objectMapper.writeValueAsString(requestWithEmptyNickname))
            .patch("/api/users/{userId}", newUser.getId())
            .then()
            .statusCode(400)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 로그인 정보가 수정할 사용자 정보와 일치하지 않으면, 403 UnAuthorized  응답을 받는다.")
    void whenNicknameIsEmptyThenStatusCodeShouldBe_403_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = createUserUpdateFilter(createIdentifier("UserUpdate", 403));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .body(objectMapper.writeValueAsString(request))
            .patch("/api/users/{userId}", Long.MAX_VALUE)
            .then()
            .statusCode(403)
            .log()
            .all();
    }
}
