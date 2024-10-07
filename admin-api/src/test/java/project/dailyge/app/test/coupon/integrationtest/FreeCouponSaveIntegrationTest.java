package project.dailyge.app.test.coupon.integrationtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.coupon.application.FreeCouponWriteUseCase;
import project.dailyge.entity.coupon.FreeCouponJpaEntity;
import project.dailyge.entity.coupon.FreeCouponReadRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("[IntegrationTest] 무료 쿠폰 저장 통합테스트")
class FreeCouponSaveIntegrationTest extends DatabaseTestBase {

    @Autowired
    private FreeCouponWriteUseCase couponWriteUseCase;

    @Autowired
    private FreeCouponReadRepository couponReadRepository;

    @Test
    @Order(1)
    @DisplayName("쿠폰이 발급되면 사용자들은 쿠폰을 가진다.")
    void whenSaveCouponsThenUsersShouldHaveCoupons() {
        final Long eventId = 1L;
        final List<Long> userIds = List.of(4L, 5L, 6L, 7L);
        couponWriteUseCase.saveAll(userIds, eventId);
        final List<FreeCouponJpaEntity> freeCoupons = couponReadRepository.findAllByEventIdAndLimit(eventId, userIds.size());
        freeCoupons.sort((one, another) -> Long.compare(one.getUserId(), another.getId()));
        final List<Long> couponWinnerIds = freeCoupons.stream().map(FreeCouponJpaEntity::getUserId).toList();
        assertEquals(userIds, couponWinnerIds);
    }
}
