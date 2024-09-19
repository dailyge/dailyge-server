package project.dailyge.app.test.coupon.documentationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.coupon.application.scheduler.CouponBulkScheduler;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipant;
import project.dailyge.app.core.coupon.persistence.CouponInMemoryRepository;
import project.dailyge.app.core.coupon.presentation.request.ScheduleRateRequest;

import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@DisplayName("[DocumentationTest] 쿠폰 벌크 스케줄러 요청 처리 작업 테스트")
class CouponBulkSchedulerDocumentationTest extends DatabaseTestBase {

    @Autowired
    private CouponInMemoryRepository couponInMemoryRepository;

    @Autowired
    private CouponBulkScheduler couponBulkScheduler;
    private ScheduleRateRequest request;

    @BeforeEach
    void setUp() {
        couponBulkScheduler.stop();
        request = new ScheduleRateRequest(5);
        for (long id = 1; id <= 100; id++) {
            final CouponEventParticipant couponEventParticipant = new CouponEventParticipant(id, System.currentTimeMillis());
            couponInMemoryRepository.save(couponEventParticipant);
        }
    }

    @Test
    @DisplayName("[RestDocs] 쿠폰 벌크 스케줄러 시작을 요청하면 201 응답을 받는다.")
    void whenStartCouponBulkSchedulerThenResponseShouldBe_201_RestDocs() throws JsonProcessingException {
        given(this.specification)
            .relaxedHTTPSValidation()
            .body(objectMapper.writeValueAsString(request))
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .post("/api/coupons/scheduling")
            .then()
            .statusCode(201)
            .log()
            .all();
    }

    @Test
    @DisplayName("[RestDocs] 쿠폰 벌크 스케줄러 정지를 요청하면 204 응답을 받는다.")
    void whenStopCouponBulksSchedulerThenResponseShouldBe_204_RestDocs() {
        given(this.specification)
            .relaxedHTTPSValidation()
            .contentType(APPLICATION_JSON_VALUE)
            .cookie(getAccessTokenCookie())
            .when()
            .delete("/api/coupons/scheduling")
            .then()
            .statusCode(204)
            .log()
            .all();
    }
}
