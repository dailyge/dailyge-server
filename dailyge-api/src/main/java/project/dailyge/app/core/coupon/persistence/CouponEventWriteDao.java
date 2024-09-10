package project.dailyge.app.core.coupon.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.core.cache.coupon.CouponCache;
import project.dailyge.core.cache.coupon.CouponCacheWriteRepository;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import static project.dailyge.common.configuration.CompressionHelper.compressAsByteArrayWithZstd;

@Repository
@RequiredArgsConstructor
class CouponEventWriteDao implements CouponCacheWriteRepository {

    private static final String COUPON_KEY = "coupon:cache";
    private static final String QUEUE_COUNT_KEY = "coupon:queue:count";
    private final RedisTemplate<String, byte[]> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void saveBulks(final List<CouponCache> couponCaches) {
        final CouponEventBulks couponEventBulks = new CouponEventBulks(couponCaches);
        try {
            redisTemplate.execute(connection -> {
                connection.openPipeline();
                couponCaches.forEach(couponCache -> redisTemplate.opsForValue().setBit(COUPON_KEY, couponCache.getUserId(), true));
                final Long count = redisTemplate.opsForValue().increment(QUEUE_COUNT_KEY);
                redisTemplate.opsForValue().set(getKey(count), compressAsByteArrayWithZstd(couponEventBulks, objectMapper));
                connection.closePipeline();
                return null;
            }, true);
        } catch (RedisException exception) {
            throw CommonException.from(exception.getMessage(), BAD_GATEWAY);
        } catch (Exception exception) {
            throw CommonException.from(exception.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteAllBulks() {
        try {
            redisTemplate.execute(connection -> {
                connection.openPipeline();
                long count = ByteBuffer.wrap(Objects.requireNonNull(redisTemplate.opsForValue().get(QUEUE_COUNT_KEY))).getLong();
                for (long queueNumber = 1; queueNumber <= count; queueNumber++) {
                    redisTemplate.delete(getKey(queueNumber));
                }
                redisTemplate.delete(QUEUE_COUNT_KEY);
                redisTemplate.opsForValue().set(COUPON_KEY, "\0".getBytes());
                connection.closePipeline();
                return null;
            }, true);
        } catch (RedisException exception) {
            throw CommonException.from(exception.getMessage(), BAD_GATEWAY);
        } catch (Exception exception) {
            throw CommonException.from(exception.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    private static String getKey(final Long queueCount) {
        return String.format("coupon:queue:%s", queueCount);
    }
}
