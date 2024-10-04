package project.dailyge.app.test.anniversary.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.anniversary.presentation.request.AnniversaryCreateRequest;
import static project.dailyge.app.test.anniversary.documentationtest.snippet.AnniversaryCreateSnippet.createAnniversaryFilter;
import static project.dailyge.app.test.anniversary.documentationtest.snippet.AnniversarySnippet.ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.anniversary.documentationtest.snippet.AnniversarySnippet.ANNIVERSARY_CREATE_REQUEST_SNIPPET;
import static project.dailyge.app.test.anniversary.documentationtest.snippet.AnniversarySnippet.ANNIVERSARY_CREATE_RESPONSE_SNIPPET;
import static project.dailyge.app.test.anniversary.documentationtest.snippet.AnniversarySnippet.createIdentifier;

import java.time.LocalDate;

@DisplayName("[DocumentationTest] 기념일 저장 문서화 테스트")
class AnniversaryCreateDocumentationTest extends DatabaseTestBase {

    @Test
    @DisplayName("[RestDocs] 기념일을 생성하면 201 응답 코드를 받는다.")
    void whenCreateAnniversaryThenStatusCodeShouldBe_201_RestDocs() throws JsonProcessingException {
        final LocalDate date = LocalDate.now();
        final AnniversaryCreateRequest request = new AnniversaryCreateRequest("부모님 결혼 기념일", date, false, null);
        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                ACCESS_TOKEN_COOKIE_SNIPPET,
                ANNIVERSARY_CREATE_REQUEST_SNIPPET,
                ANNIVERSARY_CREATE_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/anniversaries")
            .then()
            .statusCode(201);
    }

    @Test
    @DisplayName("[Swagger] 기념일을 생성하면 201 응답 코드를 받는다.")
    void whenCreateAnniversaryThenStatusCodeShouldBe_201_Swagger() throws JsonProcessingException {
        final LocalDate date = LocalDate.now();
        final AnniversaryCreateRequest request = new AnniversaryCreateRequest("부모님 결혼 기념일", date, false, null);
        final RestDocumentationFilter filter = createAnniversaryFilter(createIdentifier("AnniversaryCreate", 201));
        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/anniversaries")
            .then()
            .statusCode(201);
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 파라미터를 넘기면 400 Bad Request 응답을 받는다.")
    void whenCreateAnniversaryWithInvalidParameterThenStatusCodeShouldBe_400_Swagger() throws JsonProcessingException {
        final LocalDate date = LocalDate.now();
        final AnniversaryCreateRequest request = new AnniversaryCreateRequest(null, date, false, null);
        final RestDocumentationFilter filter = createAnniversaryFilter(createIdentifier("AnniversaryCreate", 400));
        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/anniversaries")
            .then()
            .statusCode(400);
    }
}
