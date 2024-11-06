package project.dailyge.app.test.note.documentationtest;

import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import static project.dailyge.app.common.CommonSnippet.ACCESS_TOKEN_COOKIE_SNIPPET;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.note.application.command.NoteCreateCommand;
import project.dailyge.app.core.note.facade.NoteFacade;
import static project.dailyge.app.test.note.documentationtest.snippet.NoteDeleteSnippet.createDeleteNoteFilter;
import project.dailyge.app.test.note.documentationtest.snippet.NoteSnippet;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.createIdentifier;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.identifier;
import project.dailyge.common.ratelimiter.RateLimiterWriteService;

import java.time.LocalDateTime;

@DisplayName("[DocumentationTest] Note 삭제 문서화 테스트")
class NoteDeleteDocumentationTest extends DatabaseTestBase {

    @Autowired
    private NoteFacade noteFacade;

    @Autowired
    private RateLimiterWriteService rateLimiterWriteService;

    @BeforeEach
    void setUp() {
        rateLimiterWriteService.deleteById(dailygeUser.getUserId());
    }

    @Test
    @DisplayName("[RestDocs] 발신자가 (쪽지 보관함에서) 쪽지를 삭제하면 204 No-Content 응답을 받는다.")
    void whenSenderDeleteNoteThenStatusCodeShouldBe_204_No_Content() {
        final LocalDateTime sendAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = new NoteCreateCommand(
            "주간 일정 회의", "주간 일정 회의 신청합니다.", sendAt, "kmularise", dailygeUser.getId()
        );
        final Long newNoteId = noteFacade.save(dailygeUser, command, 30);

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(identifier,
                ACCESS_TOKEN_COOKIE_SNIPPET,
                NoteSnippet.Companion.getNOTE_ID_PATH_VARIABLES_SNIPPET()
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .delete("/api/notes/{noteId}/sent", newNoteId)
            .then()
            .statusCode(204);
    }

    @Test
    @DisplayName("[Swagger] 발신자가 (쪽지 보관함에서) 쪽지를 삭제하면 204 No-Content 응답을 받는다.")
    void whenSendNoteToReceiverThenStatusCodeShouldBe_201_Swagger() {
        final LocalDateTime sendAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = new NoteCreateCommand(
            "주간 일정 회의", "주간 일정 회의 신청합니다.", sendAt, "kmularise", dailygeUser.getId()
        );
        final Long newNoteId = noteFacade.save(dailygeUser, command, 30);
        final RestDocumentationFilter filter = createDeleteNoteFilter(createIdentifier("NoteDelete", 204));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .delete("/api/notes/{noteId}/sent", newNoteId)
            .then()
            .statusCode(204);
    }

    @Test
    @DisplayName("[Swagger] (발신자 쪽지함에) 쪽지가 존재하지 않는다면 404 Not Found 응답을 받는다.")
    void whenSenderNotesNotExistsThenStatusCodeShouldBe_404_Not_Found_Swagger() {
        final Long invalidNoteId = Long.MAX_VALUE;
        final RestDocumentationFilter filter = createDeleteNoteFilter(createIdentifier("NoteDelete", 404));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .delete("/api/notes/{noteId}/sent", invalidNoteId)
            .then()
            .statusCode(404);
    }

    @Test
    @DisplayName("[RestDocs] 수신자가 (쪽지 보관함에서) 쪽지를 삭제하면 204 No-Content 응답을 받는다.")
    void whenReceiverDeleteNoteThenStatusCodeShouldBe_204_No_Content_RestDocs() {
        final LocalDateTime sendAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = new NoteCreateCommand(
            "주간 일정 회의", "주간 일정 회의 신청합니다.", sendAt, "kmularise", dailygeUser.getId()
        );
        final Long newNoteId = noteFacade.save(dailygeUser, command, 30);

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(identifier,
                ACCESS_TOKEN_COOKIE_SNIPPET,
                NoteSnippet.Companion.getNOTE_ID_PATH_VARIABLES_SNIPPET()
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getNoteReceiverAccessTokenCookie())
            .when()
            .delete("/api/notes/{noteId}/received", newNoteId)
            .then()
            .statusCode(204);
    }

    @Test
    @DisplayName("[Swagger] 수신자가 (쪽지 보관함에서) 쪽지를 삭제하면 204 No-Content 응답을 받는다.")
    void whenReceiverDeleteNoteThenStatusCodeShouldBe_204_No_Content_Swagger() {
        final LocalDateTime sendAt = LocalDateTime.of(2024, 10, 11, 13, 0);
        final NoteCreateCommand command = new NoteCreateCommand(
            "주간 일정 회의", "주간 일정 회의 신청합니다.", sendAt, "kmularise", dailygeUser.getId()
        );
        final Long newNoteId = noteFacade.save(dailygeUser, command, 30);
        final RestDocumentationFilter filter = createDeleteNoteFilter(createIdentifier("NoteDelete", 204));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getNoteReceiverAccessTokenCookie())
            .when()
            .delete("/api/notes/{noteId}/received", newNoteId)
            .then()
            .statusCode(204);
    }

    @Test
    @DisplayName("[Swagger] (수신자 쪽지함에) 쪽지가 존재하지 않는다면 404 Not Found 응답을 받는다.")
    void whenReceiverNotesNotExistsThenStatusCodeShouldBe_404_Not_Found_Swagger() {
        final Long invalidNoteId = Long.MAX_VALUE;
        final RestDocumentationFilter filter = createDeleteNoteFilter(createIdentifier("NoteDelete", 404));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getNoteReceiverAccessTokenCookie())
            .when()
            .delete("/api/notes/{noteId}/received", invalidNoteId)
            .then()
            .statusCode(404);
    }
}
