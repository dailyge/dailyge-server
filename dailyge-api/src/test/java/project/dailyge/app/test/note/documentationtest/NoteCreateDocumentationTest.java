package project.dailyge.app.test.note.documentationtest;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import com.fasterxml.jackson.core.JsonProcessingException;
import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import static project.dailyge.app.common.CommonSnippet.ACCESS_TOKEN_COOKIE_SNIPPET;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.note.application.command.NoteCreateCommand;
import project.dailyge.app.core.note.facade.NoteFacade;
import project.dailyge.app.core.note.presentation.request.NoteCreateRequest;
import project.dailyge.app.core.user.application.UserWriteService;
import static project.dailyge.app.test.note.documentationtest.snippet.NoteCreateSnippet.createNoteFilter;
import static project.dailyge.app.test.note.documentationtest.snippet.NoteSnippet.Companion;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.createIdentifier;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.identifier;
import project.dailyge.common.ratelimiter.RateLimiterWriteService;
import project.dailyge.entity.user.UserJpaEntity;

import java.time.LocalDateTime;

@DisplayName("[DocumentationTest] Note 생성 문서화 테스트")
class NoteCreateDocumentationTest extends DatabaseTestBase {

    @Autowired
    private UserWriteService userWriteService;

    @Autowired
    private NoteFacade noteFacade;

    @Autowired
    private RateLimiterWriteService rateLimiterWriteService;

    @BeforeEach
    void setUp() {
        rateLimiterWriteService.deleteById(dailygeUser.getUserId());
    }

    @Test
    @DisplayName("[RestDocs] 쪽지 전송이 성공하면 201 Created 응답을 받는다.")
    void whenSendNoteThenStatusCodeShouldBe_201_RestDocs() throws JsonProcessingException {
        final LocalDateTime date = LocalDateTime.of(2021, 10, 1, 0, 0, 0);

        // beatmejy -> kmularise
        final NoteCreateRequest request = new NoteCreateRequest("주간 일정 회의", "주간 일정 회의 신청합니다.", "kmularise", date);
        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(identifier,
                ACCESS_TOKEN_COOKIE_SNIPPET,
                Companion.getNOTE_CREATE_REQUEST_SNIPPET(),
                Companion.getNOTE_CREATE_RESPONSE_SNIPPET()
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/notes")
            .then()
            .statusCode(201);
    }

    @Test
    @DisplayName("[Swagger] 쪽지 전송이 성공하면 201 Created 응답을 받는다.")
    void whenSendNoteThenStatusCodeShouldBe_201_Swagger() throws JsonProcessingException {
        final LocalDateTime date = LocalDateTime.of(2021, 10, 1, 0, 0, 0);

        final NoteCreateRequest request = new NoteCreateRequest("주간 일정 회의", "주간 일정 회의 신청합니다.", "kmularise", date);
        final RestDocumentationFilter filter = createNoteFilter(createIdentifier("NoteCreate", 201));
        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/notes")
            .then()
            .statusCode(201)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 제목 또는 내용을 입력하면 400 Bad Request 응답을 받는다.")
    void whenInvalidParameterThenStatusCodeShouldBe_400_BadRequest() throws JsonProcessingException {
        final LocalDateTime date = LocalDateTime.of(2021, 10, 1, 0, 0, 0);

        final NoteCreateRequest request = new NoteCreateRequest("", "", "kmularise", date);
        final RestDocumentationFilter filter = createNoteFilter(createIdentifier("NoteCreate", 400));
        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/notes")
            .then()
            .statusCode(400)
            .log()
            .all();
    }

    @Test
    @DisplayName("[Swagger] 일정 기간 동안 너무 많은 쪽지를 전송하면 429 TooManyRequest 응답을 받는다.")
    void whenSendNoteInShortTimeThenStatusCodeShouldBe_429_Swagger() throws JsonProcessingException {
        final LocalDateTime date = LocalDateTime.of(2021, 10, 1, 0, 0, 0);

        final LocalDateTime sendAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = new NoteCreateCommand(
            "주간 일정 회의", "주간 일정 회의 신청합니다.", sendAt, "kmularise", dailygeUser.getId()
        );
        noteFacade.save(dailygeUser, command, 30);

        final NoteCreateRequest request = new NoteCreateRequest("주간 일정 회의", "주간 일정 회의 신청합니다.", "kmularise", date);
        final RestDocumentationFilter filter = createNoteFilter(createIdentifier("NoteCreate", 429));
        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/notes")
            .then()
            .statusCode(429)
            .log()
            .all();
    }
}
