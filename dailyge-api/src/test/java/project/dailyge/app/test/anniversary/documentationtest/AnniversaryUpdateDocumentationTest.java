package project.dailyge.app.test.anniversary.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import static io.restassured.RestAssured.given;
import static java.time.LocalDate.now;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.anniversary.application.command.AnniversaryCreateCommand;
import project.dailyge.app.core.anniversary.facade.AnniversaryFacade;
import project.dailyge.app.core.anniversary.presentation.request.AnniversaryUpdateRequest;
import static project.dailyge.app.test.anniversary.documentationtest.snippet.AnniversarySnippet.ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.anniversary.documentationtest.snippet.AnniversarySnippet.ANNIVERSARY_PATH_PARAMETER_SNIPPET;
import static project.dailyge.app.test.anniversary.documentationtest.snippet.AnniversarySnippet.ANNIVERSARY_UPDATE_REQUEST_SNIPPET;
import static project.dailyge.app.test.anniversary.documentationtest.snippet.AnniversarySnippet.ANNIVERSARY_UPDATE_RESPONSE_SNIPPET;
import static project.dailyge.app.test.anniversary.documentationtest.snippet.AnniversarySnippet.createIdentifier;
import static project.dailyge.app.test.anniversary.documentationtest.snippet.AnniversaryUpdateSnippet.createUpdateAnniversaryFilter;

import java.time.LocalDate;

@DisplayName("[DocumentationTest] 기념일 삭제 문서화 테스트")
class AnniversaryUpdateDocumentationTest extends DatabaseTestBase {

    @Autowired
    private AnniversaryFacade anniversaryFacade;

    @Test
    @DisplayName("[RestDocs] 기념일을 수정하면 200 OK 응답 코드를 받는다.")
    void whenUpdateAnniversaryThenStatusCodeShouldBe_200_OK_RestDocs() throws JsonProcessingException {
        final LocalDate date = now();
        final AnniversaryCreateCommand createCommand = new AnniversaryCreateCommand("부모님 결혼 기념일", date.atTime(0, 0), false, null);
        final Long newAnniversaryId = anniversaryFacade.save(dailygeUser, createCommand);
        final AnniversaryUpdateRequest updateRequest = new AnniversaryUpdateRequest("사진 찍기", date, true, null);

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                ACCESS_TOKEN_COOKIE_SNIPPET,
                ANNIVERSARY_PATH_PARAMETER_SNIPPET,
                ANNIVERSARY_UPDATE_REQUEST_SNIPPET,
                ANNIVERSARY_UPDATE_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(updateRequest))
            .when()
            .put("/api/anniversaries/{anniversaryId}", newAnniversaryId)
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("[Swagger] 기념일을 수정하면 200 OK 응답 코드를 받는다.")
    void whenUpdateAnniversaryThenStatusCodeShouldBe_200_OK_Swagger() throws JsonProcessingException {
        final LocalDate date = now();
        final AnniversaryCreateCommand createCommand = new AnniversaryCreateCommand("부모님 결혼 기념일", date.atTime(0, 0), false, null);
        final Long newAnniversaryId = anniversaryFacade.save(dailygeUser, createCommand);
        final AnniversaryUpdateRequest updateRequest = new AnniversaryUpdateRequest("사진 찍기", date, true, null);
        final RestDocumentationFilter filter = createUpdateAnniversaryFilter(createIdentifier("AnniversaryUpdate", 400));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(updateRequest))
            .when()
            .put("/api/anniversaries/{anniversaryId}", newAnniversaryId)
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 파라미터를 입력하면 400 Bad Request를 반환한다.")
    void whenDeleteAnniversaryWithInvalidIdThenStatusCodeShouldBe_400_Bad_Request_Swagger() throws JsonProcessingException {
        final LocalDate date = now();
        final AnniversaryCreateCommand createCommand = new AnniversaryCreateCommand("부모님 결혼 기념일", date.atTime(0, 0), false, null);
        anniversaryFacade.save(dailygeUser, createCommand);
        final AnniversaryUpdateRequest updateRequest = new AnniversaryUpdateRequest("사진 찍기", date, true, null);
        final RestDocumentationFilter filter = createUpdateAnniversaryFilter(createIdentifier("AnniversaryUpdate", 400));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(updateRequest))
            .when()
            .put("/api/anniversaries/{anniversaryId}", "invalidId")
            .then()
            .statusCode(400);
    }
}
