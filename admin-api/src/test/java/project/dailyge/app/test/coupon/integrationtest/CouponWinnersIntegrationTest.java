package project.dailyge.app.test.coupon.integrationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.coupon.application.CouponUseCase;
import project.dailyge.app.coupon.exception.CouponTypeException;
import project.dailyge.app.test.coupon.fixture.CouponFixture;
import project.dailyge.core.cache.coupon.CouponEvent;
import project.dailyge.core.cache.coupon.CouponEventWriteRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("[IntegrationTest] 선착순 쿠폰 당첨자 선정 통합테스트")
public class CouponWinnersIntegrationTest extends DatabaseTestBase {
    @Autowired
    private CouponUseCase couponUseCase;

    @Autowired
    private CouponEventWriteRepository couponEventWriteRepository;

    @Autowired
    private RedisTemplate<String, byte[]> redisTemplate;

    @BeforeEach
    void setUp() {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.serverCommands().flushAll();
            return null;
        });
        //요청 벌크들이 Redis에 저장된 상태 구현
        final List<List<CouponEvent>> couponEventLists = CouponFixture.createCouponEventLists();
        for (List<CouponEvent> couponEvents : couponEventLists) {
            couponEventWriteRepository.saveBulks(couponEvents);
        }
    }

    @Test
    @DisplayName("쿠폰 당첨자 선정 시 가장 빨리 참여한 사용자 Id 목록 반환한다.")
    void whenFindWinnersReturnEarliestUserIds() {
        final List<Long> winners = couponUseCase.findWinners(1000, 1L);
        assertEquals(CouponFixture.findExpectedUserIdsList(), winners);
    }

    @Test
    @DisplayName("이미 쿠폰 당첨자 선정을 진행했으면 CouponTypeException을 반환한다.")
    void whenAlreadyRunsWinnerSelectionThenThrows() {
        couponEventWriteRepository.increaseSelectionRunCount();
        assertThrows(CouponTypeException.class, () -> couponUseCase.findWinners(1000, 1L));
    }
}
