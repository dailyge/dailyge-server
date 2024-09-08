package project.dailyge.app.test.coupon.documentationtest;

import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import project.dailyge.app.common.DatabaseTestBase;

import java.time.LocalDateTime;

@DisplayName("[DocumentationTest] 쿠폰 발급 신청 문서화 테스트")
class CouponCreateDocumentationTest extends DatabaseTestBase {

    @BeforeEach
    void setUp() {
        nowTime = LocalDateTime.now();
    }

    @Test
    @DisplayName("[RestDocs] 쿠폰 발급에 참여하면 201 Created 응답을 받는다.")
    void whenCreateCouponThenResponseShouldBe_201_RestDocs() {
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
