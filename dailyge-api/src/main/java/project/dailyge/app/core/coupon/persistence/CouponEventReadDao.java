package project.dailyge.app.core.coupon.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.core.cache.coupon.CouponCache;
import project.dailyge.core.cache.coupon.CouponCacheReadRepository;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import static project.dailyge.common.configuration.CompressionHelper.decompressAsObjWithZstd;

@Repository
@RequiredArgsConstructor
class CouponEventReadDao implements CouponCacheReadRepository {

    private final RedisTemplate<String, byte[]> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String COUPON_KEY = "coupon:cache";
    private static final String WINNER_SELECTION_DONE_KEY = "coupon:winner_selection";
    private static final String QUEUE_COUNT_KEY = "coupon:queue:count";


    @Override
    public boolean existsByUserId(final Long userId) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForValue().getBit(COUPON_KEY, userId));
        } catch (RedisException exception) {
            throw CommonException.from(exception.getMessage(), BAD_GATEWAY);
        } catch (Exception exception) {
            throw CommonException.from(exception.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<CouponCache> findBulks(int queueNumber) {
        final CouponEventBulks couponEventBulks = decompressAsObjWithZstd(
            Objects.requireNonNull(redisTemplate.opsForValue().get(getKey(queueNumber))),
            CouponEventBulks.class, objectMapper);
        return couponEventBulks.couponCaches();
    }

    @Override
    public Integer findQueueCount() {
        return ByteBuffer.wrap(Objects.requireNonNull(redisTemplate.opsForValue().get(QUEUE_COUNT_KEY))).getInt();
    }

    private static String getKey(final int queueNumber) {
        return String.format("coupon:queue:%s", queueNumber);
    }
}
