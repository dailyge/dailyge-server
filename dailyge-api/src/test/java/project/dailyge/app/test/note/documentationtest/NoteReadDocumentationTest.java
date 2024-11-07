package project.dailyge.app.test.note.documentationtest;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static java.util.UUID.randomUUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import static project.dailyge.app.common.CommonSnippet.ACCESS_TOKEN_COOKIE_SNIPPET;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.note.facade.NoteFacade;
import project.dailyge.app.core.note.presentation.request.NoteCreateRequest;
import static project.dailyge.app.test.note.documentationtest.snippet.NoteReadSnippet.createReceivedNoteDetailReadFilter;
import project.dailyge.app.test.note.documentationtest.snippet.NoteSnippet;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.createIdentifier;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.identifier;

import java.time.LocalDateTime;

@DisplayName("[DocumentationTest] 쪽지 조회 문서화 테스트")
class NoteReadDocumentationTest extends DatabaseTestBase {

    @Autowired
    private NoteFacade noteFacade;

    @Test
    @DisplayName("[RestDocs] 발신자가 보낸 쪽지를 조회하면 200 OK 응답을 받는다.")
    void whenSenderReadSentNoteThenStatusCodeShouldBe_200_OK_RestDocs() {
        final LocalDateTime date = LocalDateTime.of(2021, 10, 1, 0, 0, 0);
        final NoteCreateRequest request = new NoteCreateRequest("주간 일정 회의", "주간 일정 회의 신청합니다.", "kmularise", date);
        final Long newNoteId = noteFacade.save(dailygeUser, request.toCommand(dailygeUser), 30);

        given(this.specification)
            .filter(document(identifier,
                ACCESS_TOKEN_COOKIE_SNIPPET,
                NoteSnippet.Companion.getNOTE_ID_PATH_VARIABLES_SNIPPET(),
                NoteSnippet.Companion.getSENT_NOTE_DETAIL_READ_RESPONSE_SNIPPET()
            ))
            .when()
            .cookie(getAccessTokenCookie())
            .get("/api/notes/{noteId}/sent", newNoteId)
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("[Swagger] 발신자가 보낸 쪽지를 조회하면 200 OK 응답을 받는다.")
    void whenSenderReadSentNoteThenStatusCodeShouldBe_200_OK_Swagger() {
        final LocalDateTime date = LocalDateTime.of(2021, 10, 1, 0, 0, 0);
        final NoteCreateRequest request = new NoteCreateRequest("주간 일정 회의", "주간 일정 회의 신청합니다.", "kmularise", date);
        final Long newNoteId = noteFacade.save(dailygeUser, request.toCommand(dailygeUser), 30);

        final RestDocumentationFilter filter = createReceivedNoteDetailReadFilter(createIdentifier("ReceivedNoteDetailReadOK", 200));
        given(this.specification)
            .filter(filter)
            .when()
            .cookie(getAccessTokenCookie())
            .get("/api/notes/{noteId}/sent", newNoteId)
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("[Swagger] 발신자가 보낸 쪽지를 조회할 때, 올바르지 않은 파라미터로 조회하면 400 Bad Request 응답을 받는다.")
    void whenSenderReadSentNoteWithInvalidParameterThenStatusCodeShouldBe_400_Bad_Request() {
        final String invalidNoteId = randomUUID().toString();
        final RestDocumentationFilter filter = createReceivedNoteDetailReadFilter(createIdentifier("ReceivedNoteDetailRead", 400));
        given(this.specification)
            .filter(filter)
            .when()
            .cookie(getAccessTokenCookie())
            .get("/api/notes/{noteId}/sent", invalidNoteId)
            .then()
            .statusCode(400);
    }

    @Test
    @DisplayName("[Swagger] 발신자가 보낸 쪽지를 조회할 때, 쪽지가 존재하지 않는다면 404 Not Found 응답을 받는다.")
    void whenSenderReadSentNoteButNoteNotExistsThenStatusCodeShouldBe_404_Not_Found() {
        final Long invalidNoteId = Long.MAX_VALUE;
        final RestDocumentationFilter filter = createReceivedNoteDetailReadFilter(createIdentifier("ReceivedNoteDetailRead", 200));
        given(this.specification)
            .filter(filter)
            .when()
            .cookie(getAccessTokenCookie())
            .get("/api/notes/{noteId}/sent", invalidNoteId)
            .then()
            .statusCode(404);
    }

    @Test
    @DisplayName("[RestDocs] 수신자가 받은 쪽지를 조회하면 200 OK 응답을 받는다.")
    void whenReceiverReadReceivedNoteThenStatusCodeShouldBe_200_OK_RestDocs() {
        final LocalDateTime date = LocalDateTime.of(2021, 10, 1, 0, 0, 0);
        final NoteCreateRequest request = new NoteCreateRequest("주간 일정 회의", "주간 일정 회의 신청합니다.", "kmularise", date);
        final Long newNoteId = noteFacade.save(dailygeUser, request.toCommand(dailygeUser), 30);

        final RestDocumentationFilter filter = createReceivedNoteDetailReadFilter(createIdentifier("ReceivedNoteDetailRead", 200));
        given(this.specification)
            .filter(filter)
            .when()
            .cookie(getNoteReceiverAccessTokenCookie())
            .get("/api/notes/{noteId}/received", newNoteId)
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("[Swagger] 수신자가 받은 쪽지를 조회하면 200 OK 응답을 받는다.")
    void whenReceiverReadReceivedNoteThenStatusCodeShouldBe_200_OK_Swagger() {
        final LocalDateTime date = LocalDateTime.of(2021, 10, 1, 0, 0, 0);
        final NoteCreateRequest request = new NoteCreateRequest("주간 일정 회의", "주간 일정 회의 신청합니다.", "kmularise", date);
        final Long newNoteId = noteFacade.save(dailygeUser, request.toCommand(dailygeUser), 30);

        given(this.specification)
            .filter(document(identifier,
                ACCESS_TOKEN_COOKIE_SNIPPET,
                NoteSnippet.Companion.getNOTE_ID_PATH_VARIABLES_SNIPPET(),
                NoteSnippet.Companion.getRECEIVED_NOTE_DETAIL_READ_RESPONSE_FIELDS_RESPONSE_SNIPPET()
            ))
            .when()
            .cookie(getNoteReceiverAccessTokenCookie())
            .get("/api/notes/{noteId}/received", newNoteId)
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("[Swagger] 수신자가 받은 쪽지를 조회할 때, 올바르지 않은 파라미터로 조회하면 400 Bad Request 응답을 받는다.")
    void whenReceiverReadReceivedNoteWithInvalidParameterThenStatusCodeShouldBe_400_Bad_Request() {
        final String invalidNoteId = randomUUID().toString();

        final RestDocumentationFilter filter = createReceivedNoteDetailReadFilter(createIdentifier("ReceivedNoteDetailRead", 400));
        given(this.specification)
            .filter(filter)
            .when()
            .cookie(getNoteReceiverAccessTokenCookie())
            .get("/api/notes/{noteId}/received", invalidNoteId)
            .then()
            .statusCode(400);
    }

    @Test
    @DisplayName("[Swagger] 수신자가 받은 쪽지를 조회할 때, 쪽지가 없다면 404 Not Found 응답을 받는다.")
    void whenReceiverReadReceivedNoteButNoteNotExistsThenStatusCodeShouldBe_404_Not_Found() {
        final Long invalidNoteId = Long.MAX_VALUE;

        final RestDocumentationFilter filter = createReceivedNoteDetailReadFilter(createIdentifier("ReceivedNoteDetailRead", 404));
        given(this.specification)
            .filter(filter)
            .when()
            .cookie(getNoteReceiverAccessTokenCookie())
            .get("/api/notes/{noteId}/received", invalidNoteId)
            .then()
            .statusCode(404);
    }
}
