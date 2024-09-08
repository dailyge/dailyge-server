package project.dailyge.app.test.coupon.integrationtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.coupon.persistence.CouponCacheBulks;
import project.dailyge.app.core.coupon.persistence.CouponCacheReadDao;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipant;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipantRepository;
import project.dailyge.common.configuration.CompressionHelper;
import project.dailyge.core.cache.coupon.CouponCacheWriteUseCase;
import project.dailyge.document.common.UuidGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[IntegrationTest] 쿠폰 요청 벌크 처리 통합 테스트")
public class CouponBulksIntegrationTest extends DatabaseTestBase {
    private static final Logger log = LoggerFactory.getLogger(CouponBulksIntegrationTest.class);

    @Autowired
    private CouponCacheWriteUseCase couponCacheWriteUseCase;

    @Autowired
    private CouponEventParticipantRepository couponEventParticipantRepository;

    @Autowired
    private CouponCacheReadDao couponCacheReadDao;

    @Autowired
    RedisTemplate<String, byte[]> redisTemplate;


    @Test
    @DisplayName("인메모리큐에 있던 쿠폰 요청 벌크들이 있으면 Redis에 저장된다.")
    void whenCouponBulksExistsThenRedisSave() {
        couponEventParticipantRepository.deleteAll();
        final long maxId = 10;
        log.info("memory queue size:{}", couponEventParticipantRepository.count());
        for (long id = 1; id <= maxId; id++) {
            couponEventParticipantRepository.save(new CouponEventParticipant(id, UuidGenerator.createTimeStamp()));
        }
        couponCacheWriteUseCase.saveBulks();
        log.info("memory queue size:{}", couponEventParticipantRepository.count());
        assertEquals(0, couponEventParticipantRepository.count());
        for (long id = 1; id <= maxId; id++) {
            assertTrue(couponCacheReadDao.existsByUserId(maxId));
        }
        final String targetKey = "coupon:queue:1";
        final CouponCacheBulks actualCouponCacheBulks = CompressionHelper.decompressAsObjWithZstd(redisTemplate.opsForValue().get(targetKey), CouponCacheBulks.class, objectMapper);
        assertEquals(maxId, actualCouponCacheBulks.couponCaches().size());
    }
}
