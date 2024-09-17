package project.dailyge.app.test.event.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.event.persistence.EventCache;
import project.dailyge.app.core.event.persistence.LocalEventCache;

import static io.restassured.RestAssured.given;
import static java.time.LocalDateTime.now;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@DisplayName("[DocumentationTest] 이벤트 참여 문서화 테스트")
class CouponCreateDocumentationTest extends DatabaseTestBase {

    @Autowired
    private LocalEventCache localEventCache;

    @BeforeEach
    void setUp() {
        localEventCache.save(new EventCache(1L, now().minusHours(1), now().plusMonths(1), 0L));
    }

    @Test
    @DisplayName("[RestDocs] 쿠폰 이벤트에 참여하면 201 Created 응답을 받는다.")
    void whenParticipateCouponEventThenResponseShouldBe_201_RestDocs() throws JsonProcessingException {
        given(this.specification)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .post("/api/events/1")
            .then()
            .statusCode(201)
            .log()
            .all();
    }
}
