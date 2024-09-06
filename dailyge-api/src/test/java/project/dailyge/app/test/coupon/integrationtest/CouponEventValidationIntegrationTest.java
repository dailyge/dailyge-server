package project.dailyge.app.test.coupon.integrationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.coupon.persistence.CouponCacheReadDao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[IntegrationTest] 쿠폰 발급 유효성 검증 통합 테스트")
class CouponEventValidationIntegrationTest extends DatabaseTestBase {
    private static final int USER_CAPACITY = 1_000_000;
    private static final String KEY = "user:coupon";

    @Autowired
    private RedisTemplate<String, byte[]> redisTemplate;

    @Autowired
    private CouponCacheReadDao couponCacheReadDao;

    @BeforeEach
    void setUp() {
        redisTemplate.execute(connection -> {
            connection.openPipeline();
            for (long participantId = 4L; participantId < USER_CAPACITY; participantId += 100L) {
                redisTemplate.opsForValue().setBit(KEY, participantId, true);
            }
            connection.closePipeline();
            return null;
        }, true);
    }

    @Test
    @DisplayName("쿠폰 발급 신청 참여 여부를 확인한다.")
    void whenCallCouponCacheReadDaoReturnParticipationStatus() {
        final long participantId = 4L;
        final long invalidParticipantId = 3L;
        final boolean validBit = couponCacheReadDao.existsByUserId(participantId);
        final boolean invalidBit = couponCacheReadDao.existsByUserId(invalidParticipantId);
        assertTrue(validBit);
        assertFalse(invalidBit);
    }
}
