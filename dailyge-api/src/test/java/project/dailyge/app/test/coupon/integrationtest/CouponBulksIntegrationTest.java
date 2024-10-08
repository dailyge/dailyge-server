package project.dailyge.app.test.coupon.integrationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipant;
import project.dailyge.app.core.coupon.persistence.CouponInMemoryRepository;
import project.dailyge.core.cache.coupon.CouponEvent;
import project.dailyge.core.cache.coupon.CouponEventReadRepository;
import project.dailyge.core.cache.coupon.CouponEventWriteService;
import project.dailyge.document.common.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[IntegrationTest] 쿠폰 요청 벌크 처리 통합 테스트")
class CouponBulksIntegrationTest extends DatabaseTestBase {

    @Autowired
    private CouponEventWriteService couponEventWriteService;

    @Autowired
    private CouponInMemoryRepository couponInMemoryRepository;

    @Autowired
    private CouponEventReadRepository couponCacheReadDao;

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
    @DisplayName("인메모리큐에 있던 쿠폰 요청 벌크들이 있으면 Redis 키로 쿠폰 요청 벌크들을 이동시키고 사용자 참여여부를 표시한다.")
    void whenCouponBulksExistsThenRedisSaveBulks() {
        final long maxId = 10;
        final List<CouponEvent> expectedCouponEvents = new ArrayList<>();
        for (long id = 1; id <= maxId; id++) {
            final long timestamp = UuidGenerator.createTimeStamp();
            couponInMemoryRepository.save(new CouponEventParticipant(id, timestamp));
            expectedCouponEvents.add(new CouponEvent(id, timestamp));
        }
        couponEventWriteService.saveBulks();
        assertEquals(0, couponInMemoryRepository.count());
        for (long id = 1; id <= maxId; id++) {
            assertTrue(couponCacheReadDao.existsByUserId(id, 1L));
        }
        couponCacheReadDao.findBulks(1, (int) maxId, 1L);
        assertEquals(expectedCouponEvents, couponCacheReadDao.findBulks(1, (int) maxId, 1L));
    }

    @Test
    @DisplayName("인메모리에 있던 다수의 쿠폰 요청 벌크들을 Redis 키들의 값으로 이동시킨다.")
    void whenMultipleCouponBulksExistsThenRedisShouldSaveBulks() {
        final long maxId = 10;
        final List<List<CouponEvent>> expectedCouponBulksQueues = new ArrayList<>();
        final int totalCount = 2;
        for (int count = 1; count <= totalCount; count++) {
            final List<CouponEvent> expectedCouponEvents = new ArrayList<>();
            for (long id = 1; id <= maxId; id++) {
                final long timestamp = UuidGenerator.createTimeStamp();
                couponInMemoryRepository.save(new CouponEventParticipant(id, timestamp));
                expectedCouponEvents.add(new CouponEvent(id, timestamp));
            }
            expectedCouponBulksQueues.add(expectedCouponEvents);
            couponEventWriteService.saveBulks();
        }
        for (int number = 1; number <= totalCount; number++) {
            assertEquals(expectedCouponBulksQueues.get(number - 1), couponCacheReadDao.findBulks(number, (int) maxId, 1L));
        }
    }
}
