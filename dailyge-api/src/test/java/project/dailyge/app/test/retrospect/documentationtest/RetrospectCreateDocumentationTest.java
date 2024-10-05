package project.dailyge.app.test.retrospect.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.retrospect.presentation.request.RetrospectCreateRequest;
import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static project.dailyge.app.test.retrospect.documentationtest.snippet.RetrospectCreateSnippet.createRetrospectFilter;
import static project.dailyge.app.test.retrospect.documentationtest.snippet.RetrospectSnippet.ACCESS_TOKEN_COOKIE_SNIPPET;
import static project.dailyge.app.test.retrospect.documentationtest.snippet.RetrospectSnippet.RETROSPECT_CREATE_REQUEST_SNIPPET;
import static project.dailyge.app.test.retrospect.documentationtest.snippet.RetrospectSnippet.RETROSPECT_CREATE_RESPONSE_SNIPPET;
import static project.dailyge.app.test.retrospect.documentationtest.snippet.RetrospectSnippet.createIdentifier;

@DisplayName("[DocumentationTest] 회고 저장 문서화 테스트")
class RetrospectCreateDocumentationTest extends DatabaseTestBase {

    private RetrospectCreateRequest request;

    @BeforeEach
    void setUp() {
        final LocalDate date = LocalDate.now();
        request = new RetrospectCreateRequest("회고 제목", "회고 내용", date, true);
    }

    @Test
    @DisplayName("[RestDocs] 회고를 생성하면 201 응답 코드를 받는다.")
    void whenCreateRetrospectThenStatusCodeShouldBe_201_RestDocs() throws JsonProcessingException {
        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(document(IDENTIFIER,
                ACCESS_TOKEN_COOKIE_SNIPPET,
                RETROSPECT_CREATE_REQUEST_SNIPPET,
                RETROSPECT_CREATE_RESPONSE_SNIPPET
            ))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/retrospects")
            .then()
            .statusCode(201);
    }

    @Test
    @DisplayName("[Swagger] 회고를 생성하면 201 응답 코드를 받는다.")
    void whenCreateRetrospectThenStatusCodeShouldBe_201_Swagger() throws JsonProcessingException {
        final RestDocumentationFilter filter = createRetrospectFilter(createIdentifier("RetrospectCreate", 201));
        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(request))
            .when()
            .post("/api/retrospects")
            .then()
            .statusCode(201);
    }

    @Test
    @DisplayName("[Swagger] 올바르지 않은 파라미터를 넘기면 400 Bad Request 응답을 받는다.")
    void whenCreateRetrospectWithInvalidParameterThenStatusCodeShouldBe_400_Swagger() throws JsonProcessingException {
        final LocalDate date = LocalDate.now();
        final RetrospectCreateRequest newRequest = new RetrospectCreateRequest(null, null, date, true);

        final RestDocumentationFilter filter = createRetrospectFilter(createIdentifier("RetrospectCreate", 400));
        given(this.specification)
            .relaxedHTTPSValidation()
            .filter(filter)
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .body(objectMapper.writeValueAsString(newRequest))
            .when()
            .post("/api/retrospects")
            .then()
            .statusCode(400);
    }
}
