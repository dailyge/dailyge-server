package project.dailyge.app.test.retrospect.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.retrospect.application.RetrospectWriteService;
import project.dailyge.app.core.retrospect.presentation.request.RetrospectCreateRequest;
import project.dailyge.app.core.retrospect.presentation.request.RetrospectUpdateRequest;
import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static project.dailyge.app.test.retrospect.documentationtest.snippet.RetrospectSnippet.ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.retrospect.documentationtest.snippet.RetrospectSnippet.RETROSPECT_UPDATE_REQUEST_SNIPPET;
import static project.dailyge.app.test.retrospect.documentationtest.snippet.RetrospectSnippet.RETROSPECT_UPDATE_RESPONSE_SNIPPET;
import static project.dailyge.app.test.retrospect.documentationtest.snippet.RetrospectSnippet.createIdentifier;
import static project.dailyge.app.test.retrospect.documentationtest.snippet.RetrospectUpdateSnippet.createUpdateRetrospectFilter;

@DisplayName("[DocumentationTest] 회고 수정 문서화 테스트")
class RetrospectUpdateDocumentationTest extends DatabaseTestBase {

    private RetrospectUpdateRequest updateRequest;
    private Long retrospectId;

    @Autowired
    private RetrospectWriteService retrospectWriteService;

    @BeforeEach
    void setUp() {
        final RetrospectCreateRequest createRequest = new RetrospectCreateRequest("회고 제목", "회고 내용", LocalDate.now(), false);
        retrospectId = retrospectWriteService.save(dailygeUser, createRequest.toCommand());
        updateRequest = new RetrospectUpdateRequest("회고 제목 수정", "회고 내용 수정", LocalDate.now(), true);
    }

    @Test
    @DisplayName("[RestDocs] 회고를 수정하면 200 응답 코드를 받는다.")
    void whenUpdateRetrospectThenStatusCodeShouldBe_200_RestDocs() throws JsonProcessingException {
        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                ACCESS_TOKEN_COOKIE_SNIPPET,
                RETROSPECT_UPDATE_REQUEST_SNIPPET,
                RETROSPECT_UPDATE_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(updateRequest))
            .when()
            .put("/api/retrospects/{retrospectId}", retrospectId)
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("[Swagger] 회고를 수정하면 200 응답 코드를 받는다.")
    void whenUpdateRetrospectThenStatusCodeShouldBe_200_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = createUpdateRetrospectFilter(createIdentifier("RetrospectUpdate", 200));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(updateRequest))
            .when()
            .put("/api/retrospects/{retrospectId}", retrospectId)
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 파라미터를 넘기면 400 Bad Request 응답을 받는다.")
    void whenUpdateRetrospectWithInvalidParameterThenStatusCodeShouldBe_400_Swagger() throws JsonProcessingException {
        final LocalDate date = LocalDate.now();
        final RetrospectUpdateRequest newRequest = new RetrospectUpdateRequest(null, null, date, true);
        final RestDocumentationFilter filter = createUpdateRetrospectFilter(createIdentifier("RetrospectUpdate", 400));

        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(newRequest))
            .when()
            .put("/api/retrospects/{retrospectId}", retrospectId)
            .then()
            .statusCode(400);
    }
}
