package project.dailyge.app.test.coupon.integrationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipant;
import project.dailyge.app.core.coupon.persistence.CouponInMemoryRepository;
import project.dailyge.core.cache.coupon.CouponCache;
import project.dailyge.core.cache.coupon.CouponCacheReadRepository;
import project.dailyge.core.cache.coupon.CouponCacheWriteUseCase;
import project.dailyge.document.common.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[IntegrationTest] 쿠폰 요청 벌크 처리 통합 테스트")
class CouponBulksIntegrationTest extends DatabaseTestBase {

    @Autowired
    private CouponCacheWriteUseCase couponCacheWriteUseCase;

    @Autowired
    private CouponInMemoryRepository couponInMemoryRepository;

    @Autowired
    private CouponCacheReadRepository couponCacheReadDao;

    @Autowired
    private RedisTemplate<String, byte[]> redisTemplate;

    @BeforeEach
    void setUp() {
        couponInMemoryRepository.deleteAll();
        final Set<String> keys = redisTemplate.keys("*");
        if (keys != null) {
            redisTemplate.delete(keys);
        }
    }

    @Test
    @DisplayName("인메모리큐에 있던 쿠폰 요청 벌크들이 있으면 Redis에 저장된다.")
    void whenCouponBulksExistsThenRedisSave() {
        final long maxId = 10;
        final List<CouponCache> expectedCouponEvents = new ArrayList<>();
        for (long id = 1; id <= maxId; id++) {
            final long timestamp = UuidGenerator.createTimeStamp();
            couponInMemoryRepository.save(new CouponEventParticipant(id, timestamp));
            expectedCouponEvents.add(new CouponCache(id, timestamp));
        }
        couponCacheWriteUseCase.saveBulks();
        assertEquals(0, couponInMemoryRepository.count());
        for (long id = 1; id <= maxId; id++) {
            assertTrue(couponCacheReadDao.existsByUserId(id, 1L));
        }
        couponCacheReadDao.findBulks(1, (int) maxId, 1L);
        assertEquals(expectedCouponEvents, couponCacheReadDao.findBulks(1, (int) maxId, 1L));
    }

    @Test
    @DisplayName("인메모리에 있던 쿠폰 요청 벌크들이 있으면 Redis에 저장된다.")
    void whenMultipleCouponBulksExistsThenRedisShouldSave() {
        final long maxId = 10;
        final List<List<CouponCache>> expectedCouponBulksQueues = new ArrayList<>();
        final int totalCount = 2;
        for (int count = 1; count <= totalCount; count++) {
            final List<CouponCache> expectedCouponEvents = new ArrayList<>();
            for (long id = 1; id <= maxId; id++) {
                final long timestamp = UuidGenerator.createTimeStamp();
                couponInMemoryRepository.save(new CouponEventParticipant(id, timestamp));
                expectedCouponEvents.add(new CouponCache(id, timestamp));
            }
            expectedCouponBulksQueues.add(expectedCouponEvents);
            couponCacheWriteUseCase.saveBulks();
        }
        for (int number = 1; number <= totalCount; number++) {
            assertEquals(expectedCouponBulksQueues.get(number - 1), couponCacheReadDao.findBulks(number, (int) maxId, 1L));
        }
    }
}
