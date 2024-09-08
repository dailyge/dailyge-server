package project.dailyge.app.test.coupon.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.common.DatabaseTestBase;

import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@DisplayName("[DocumentationTest] [DocumentationTest] 쿠폰 발급 신청 문서화 테스트")
class CouponCreateDocumentationTest extends DatabaseTestBase {

    @Test
    @DisplayName("[RestDocs] 쿠폰 발급에 참여하면 201 Created 응답을 받는다.")
    void whenCreateCouponThenResponseShouldBe_201_RestDocs() throws JsonProcessingException {
        given(this.specification)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .post("/api/coupons")
            .then()
            .statusCode(201)
            .log()
            .all();
    }
}
