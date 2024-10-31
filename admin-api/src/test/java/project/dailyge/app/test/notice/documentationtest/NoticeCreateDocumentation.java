package project.dailyge.app.test.notice.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.auth.DailygeToken;
import project.dailyge.app.common.auth.TokenProvider;
import project.dailyge.app.core.notice.presentation.request.NoticeCreateRequest;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheWriteService;
import project.dailyge.entity.user.Role;

import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static project.dailyge.app.test.notice.documentationtest.snippet.NoticeCreateSnippet.createNoticeCreateFilter;
import static project.dailyge.app.test.notice.documentationtest.snippet.NoticeSnippet.NOTICE_ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.notice.documentationtest.snippet.NoticeSnippet.NOTICE_CREATE_REQUEST_FIELDS_SNIPPET;
import static project.dailyge.app.test.notice.documentationtest.snippet.NoticeSnippet.NOTICE_CREATE_RESPONSE_FIELDS_SNIPPET;
import static project.dailyge.app.test.notice.documentationtest.snippet.NoticeSnippet.createIdentifier;
import static project.dailyge.entity.notice.NoticeType.UPDATE;

@Disabled
@DisplayName("[DocumentationTest] Notice 등록 문서화 테스트")
class NoticeCreateDocumentation extends DatabaseTestBase {

    private static final Long ADMIN_ID = 2L;
    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String CONTENT = "내용";
    private Cookie adminCookie;
    private DailygeToken token;
    private NoticeCreateRequest request;

    @Autowired
    private UserCacheWriteService userCacheWriteService;

    @Autowired
    private TokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        final UserCache adminUser = new UserCache(ADMIN_ID, "admin", ADMIN_EMAIL, "", Role.ADMIN.name());
        userCacheWriteService.save(adminUser);
        token = tokenProvider.createToken(ADMIN_ID);
        request = new NoticeCreateRequest("공지사항 제목", CONTENT, UPDATE);
        adminCookie = new Cookie.Builder("Access-Token", token.accessToken()).build();
    }

    @Test
    @DisplayName("[RestDocs] 공지사항을 작성하면 201 Created 응답을 받는다.")
    void whenSaveNoticeThenResultShouldBe_201_RestDocs() throws JsonProcessingException {
        given(this.specification)
            .filter(document(IDENTIFIER,
                NOTICE_ACCESS_TOKEN_COOKIE_SNIPPET,
                NOTICE_CREATE_REQUEST_FIELDS_SNIPPET,
                NOTICE_CREATE_RESPONSE_FIELDS_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(adminCookie)
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/notice")
            .then()
            .statusCode(201);
    }

    @Test
    @DisplayName("[Swagger] 공지사항을 작성하면 201 Created 응답을 받는다.")
    void whenSaveNoticeThenResultShouldBe_201_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = createNoticeCreateFilter(
            createIdentifier("NoticeCreate", 201)
        );

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(adminCookie)
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/notice")
            .then()
            .statusCode(201);
    }

    @Test
    @DisplayName("[Swagger] 공지사항 등록 시 제목을 입력하지 않으면, 400 Bad Request 응답을 받는다.")
    void whenSaveNoticeThenResultShouldBe_400_Swagger() throws JsonProcessingException {
        final NoticeCreateRequest requestWithEmptyTitle = new NoticeCreateRequest("", CONTENT, UPDATE);
        final RestDocumentationFilter filter = createNoticeCreateFilter(
            createIdentifier("NoticeCreate", 400)
        );

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(adminCookie)
            .body(objectMapper.writeValueAsString(requestWithEmptyTitle))
            .when()
            .post("/api/notice")
            .then()
            .statusCode(400);
    }

    @Test
    @DisplayName("[Swagger] 어드민이 아니라면, 403 UN AUTHORIZED를 반환한다.")
    void whenSaveNoticeThenResultShouldBe_403_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = createNoticeCreateFilter(
            createIdentifier("NoticeCreate", 403)
        );

        given(this.specification)
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(AUTHORIZATION, getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/notice")
            .then()
            .statusCode(403);
    }
}
