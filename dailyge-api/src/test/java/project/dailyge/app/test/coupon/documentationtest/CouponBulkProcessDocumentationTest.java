package project.dailyge.app.test.coupon.documentationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipant;
import project.dailyge.app.core.coupon.persistence.CouponInMemoryRepository;

import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@DisplayName("[DocumentationTest] 쿠폰 벌크 요청 처리 작업 테스트")
class CouponBulkProcessDocumentationTest extends DatabaseTestBase {

    @Autowired
    private CouponInMemoryRepository couponInMemoryRepository;

    @BeforeEach
    void setUp() {
        for (long id = 1; id <= 100; id++) {
            final CouponEventParticipant couponEventParticipant = new CouponEventParticipant(id, System.currentTimeMillis());
            couponInMemoryRepository.save(couponEventParticipant);
        }
    }

    @Test
    @DisplayName("[RestDocs] 쿠폰 벌크 처리를 요청하면 201 응답을 받는다.")
    void whenProcessCouponBulksThenResponseShouldBe_201_RestDocs() {
        given(this.specification)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .post("/api/coupons/scheduling")
            .then()
            .statusCode(201)
            .log()
            .all();
    }
}
