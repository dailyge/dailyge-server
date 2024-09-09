package project.dailyge.app.test.coupon.integrationtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.coupon.application.CouponWriteUseCase;
import project.dailyge.app.core.coupon.persistence.CouponInMemoryRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("[IntegrationTest] 쿠폰 발급 신청 통합 테스트")
class CouponEventParticipantIntegrationTest extends DatabaseTestBase {
    private static final Logger log = LoggerFactory.getLogger(CouponEventParticipantIntegrationTest.class);
    @Autowired
    private CouponWriteUseCase couponWriteUseCase;

    @Autowired
    private CouponInMemoryRepository couponInMemoryRepository;

    @Test
    @DisplayName("쿠폰 발급 신청 정보를 저장하면 쿠폰 발급 저장소에 저장된다.")
    void whenCreateCouponEventParticipationThenSavedInMemoryQueue() {
        couponInMemoryRepository.deleteAll();
        log.info("쿠폰 발급 전 count: {}", couponInMemoryRepository.count());
        couponWriteUseCase.saveApply(dailygeUser);
        log.info("쿠폰 발급 후 count: {}", couponInMemoryRepository.count());
        assertEquals(1, couponInMemoryRepository.count());

    }
}
