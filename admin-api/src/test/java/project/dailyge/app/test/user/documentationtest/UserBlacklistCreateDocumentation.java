package project.dailyge.app.test.user.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import static io.restassured.RestAssured.given;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.test.user.documentationtest.snippet.UserBlacklistSnippet;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.USER_BLACKLIST_ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.USER_BLACKLIST_CREATE_PATH_PARAMETER_SNIPPET;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.USER_BLACKLIST_CREATE_RESPONSE_FIELDS_SNIPPET;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.USER_BLACKLIST_CREATE_RESPONSE_SNIPPET;
import static project.dailyge.app.test.user.documentationtest.snippet.UserSnippet.createIdentifier;
import project.dailyge.app.user.presentation.request.UserBlacklistCreateRequest;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheWriteUseCase;
import project.dailyge.entity.user.Role;

@DisplayName("[DocumentationTest] User Blacklist 등록 문서화 테스트")
class UserBlacklistCreateDocumentation extends DatabaseTestBase {

    private static final Long ADMIN_ID = 2L;
    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private Cookie adminCookie;
    private DailygeToken token;
    private UserBlacklistCreateRequest request;

    @Autowired
    private UserCacheWriteUseCase userCacheWriteUseCase;

    @Autowired
    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        final UserCache admin = new UserCache(ADMIN_ID, "admin", ADMIN_EMAIL, "", Role.ADMIN.name());
        userCacheWriteUseCase.save(admin);
        token = tokenProvider.createToken(ADMIN_ID, ADMIN_EMAIL);
        request = new UserBlacklistCreateRequest("accessToken");
        adminCookie = new Cookie.Builder("Access-Token", token.accessToken()).build();
    }

    @Test
    @Disabled
    @DisplayName("[RestDocs] 요청한 사용자를 블랙리스트에 넣는다.")
    void whenSaveUserBlacklistThenResultShouldBe_200_RestDocs() throws JsonProcessingException {
        given(this.specification)
            .filter(document(IDENTIFIER,
                USER_BLACKLIST_ACCESS_TOKEN_COOKIE_SNIPPET,
                USER_BLACKLIST_CREATE_PATH_PARAMETER_SNIPPET,
                USER_BLACKLIST_CREATE_RESPONSE_SNIPPET,
                USER_BLACKLIST_CREATE_RESPONSE_FIELDS_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(adminCookie)
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/user/blacklist/{userId}", 1L)
            .then()
            .statusCode(200);
    }

    @Test
    @Disabled
    @DisplayName("[Swagger] 요청한 사용자를 블랙리스트에 넣는다.")
    void whenSaveUserBlacklistThenResultShouldBe_200_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = UserBlacklistSnippet.createUserBlacklistCreateFilter(createIdentifier("UserBlacklistCreate", 200));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(adminCookie)
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/user/blacklist/{userId}", 1L)
            .then()
            .statusCode(200);
    }

    @Test
    @Disabled
    @DisplayName("[Swagger] 어드민이 아니라면, 403 UN AUTHORIZED를 반환한다.")
    void whenSaveUserBlacklistThenResultShouldBe_403_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = UserBlacklistSnippet.createUserBlacklistCreateFilter(createIdentifier("UserBlacklistCreate", 403));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(AUTHORIZATION, getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/user/blacklist/{userId}", Long.MAX_VALUE)
            .then()
            .statusCode(403);
    }

    @Test
    @Disabled
    @DisplayName("[Swagger] 요청한 사용자가 존재하지 않는다면, 404 NOT FOUND를 반환한다.")
    void whenSaveUserBlacklistThenResultShouldBe_404_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = UserBlacklistSnippet.createUserBlacklistCreateFilter(createIdentifier("UserBlacklistCreate", 404));

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(adminCookie)
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/user/blacklist/{userId}", Long.MAX_VALUE)
            .then()
            .statusCode(404);
    }
}
