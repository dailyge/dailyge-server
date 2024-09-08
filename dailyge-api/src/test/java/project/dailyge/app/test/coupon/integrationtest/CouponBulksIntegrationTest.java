package project.dailyge.app.test.coupon.integrationtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.coupon.persistence.CouponEventBulks;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipant;
import project.dailyge.app.core.coupon.persistence.CouponEventReadDao;
import project.dailyge.app.core.coupon.persistence.CouponInMemoryRepository;
import project.dailyge.common.configuration.CompressionHelper;
import project.dailyge.core.cache.coupon.CouponCacheWriteUseCase;
import project.dailyge.document.common.UuidGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[IntegrationTest] 쿠폰 요청 벌크 처리 통합 테스트")
class CouponBulksIntegrationTest extends DatabaseTestBase {
    private static final Logger log = LoggerFactory.getLogger(CouponBulksIntegrationTest.class);
    private static final String KEY = "coupon:cache";

    @Autowired
    private CouponCacheWriteUseCase couponCacheWriteUseCase;

    @Autowired
    private CouponInMemoryRepository couponInMemoryRepository;

    @Autowired
    private CouponEventReadDao couponCacheReadDao;

    @Autowired
    private RedisTemplate<String, byte[]> redisTemplate;


    @Test
    @DisplayName("인메모리큐에 있던 쿠폰 요청 벌크들이 있으면 Redis에 저장된다.")
    void whenCouponBulksExistsThenRedisSave() {
        final String targetKey = "coupon:queue:1";
        redisTemplate.delete(KEY);
        redisTemplate.opsForValue().set(KEY, "\0" .getBytes());
        redisTemplate.delete(targetKey);
        redisTemplate.delete("coupon:queue:count");
        couponInMemoryRepository.deleteAll();
        final long maxId = 10;
        log.info("memory queue size:{}", couponInMemoryRepository.count());
        for (long id = 1; id <= maxId; id++) {
            couponInMemoryRepository.save(new CouponEventParticipant(id, UuidGenerator.createTimeStamp()));
        }
        couponCacheWriteUseCase.saveBulks();
        log.info("memory queue size:{}", couponInMemoryRepository.count());
        assertEquals(0, couponInMemoryRepository.count());
        for (long id = 1; id <= maxId; id++) {
            assertTrue(couponCacheReadDao.existsByUserId(maxId));
        }
        final CouponEventBulks actualCouponEventBulks = CompressionHelper.decompressAsObjWithZstd(redisTemplate.opsForValue().get(targetKey), CouponEventBulks.class, objectMapper);
        assertEquals(maxId, actualCouponEventBulks.couponCaches().size());
    }
}
