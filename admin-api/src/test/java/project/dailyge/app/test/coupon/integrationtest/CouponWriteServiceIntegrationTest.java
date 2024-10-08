package project.dailyge.app.test.coupon.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.coupon.application.CouponEventWriteService;
import project.dailyge.app.test.coupon.fixture.CouponWinnerFixture;
import project.dailyge.core.cache.coupon.CouponEvent;
import project.dailyge.core.cache.coupon.CouponEventWriteRepository;

import java.util.List;
import java.util.Set;

@DisplayName("[IntegrationTest] 당첨자 선정 통합 테스트 ")
class CouponWriteServiceIntegrationTest extends DatabaseTestBase {

    @Autowired
    private CouponEventWriteService couponEventWriteService;

    @Autowired
    private CouponEventWriteRepository couponEventWriteRepository;

    @Autowired
    private RedisTemplate<String, byte[]> redisTemplate;

    @BeforeEach
    void setUp() {
        final Set<String> keys = redisTemplate.keys("*");
        if (keys != null) {
            redisTemplate.delete(keys);
        }
    }

    @Test
    @DisplayName("당첨자 선정작업을 시작하면 당첨자를 적절하게 선정해야 한다.")
    void whenExecuteWinnerSelectionThenResultShouldBeCorrect() {
        final int totalCount = 10000;
        final int queueCount = 10;
        final int winnerCount = 1000;
        final List<CouponEvent> couponWinners = CouponWinnerFixture.makeExpectedCouponEvents(winnerCount);
        final List<Long> expectedUserIds = couponWinners.stream().map(CouponEvent::getUserId).toList();
        final List<List<CouponEvent>> candidates = CouponWinnerFixture.makeRandomData(totalCount, queueCount, couponWinners);
        for (List<CouponEvent> couponEvents : candidates) {
            couponEventWriteRepository.saveBulks(couponEvents, 1L);
        }
        couponEventWriteService.pickWinners(1000, 1L);
        //TODO: 당첨자에 대해 쿠폰 발급 추가 시 검증
        final List<Long> actualUserIds = couponEventWriteService.pickWinners(1000, 1L);
        actualUserIds.sort(Long::compare);
        assertEquals(expectedUserIds, actualUserIds);
    }
}
