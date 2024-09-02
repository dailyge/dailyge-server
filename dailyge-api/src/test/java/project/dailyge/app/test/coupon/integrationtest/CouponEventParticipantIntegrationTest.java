package project.dailyge.app.test.coupon.integrationtest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.coupon.application.CouponWriteUseCase;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipantRepository;

@DisplayName("[IntegrationTest] 쿠폰 발급 신청 통합 테스트")
class CouponEventParticipantIntegrationTest extends DatabaseTestBase {
    @Autowired
    private CouponWriteUseCase couponWriteUseCase;

    @Autowired
    private CouponEventParticipantRepository couponEventParticipantRepository;

    @Test
    @DisplayName("쿠폰 발급 신청 정보를 저장하면 쿠폰 발급 저장소에 저장된다.")
    void whenCreateCouponEventParticipationThenSavedInMemoryQueue() {
        couponWriteUseCase.saveApply(dailygeUser);
        Assertions.assertEquals(1, couponEventParticipantRepository.count());
    }
}
