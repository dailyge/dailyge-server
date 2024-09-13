package project.dailyge.app.core.coupon.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.core.cache.coupon.CouponEvent;
import project.dailyge.core.cache.coupon.CouponEventWriteRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import static project.dailyge.common.configuration.CompressionHelper.compressAsByteArrayWithZstd;

@Repository
@RequiredArgsConstructor
class CouponEventWriteDao implements CouponEventWriteRepository {

    private static final String COUPON_KEY = "coupon:cache";
    private static final String QUEUE_COUNT_KEY = "coupon:queue:count";
    private static final String WINNER_SELECTION_DONE_KEY = "coupon:winner_selection";
    private final RedisTemplate<String, byte[]> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void saveBulks(final List<CouponEvent> couponCaches) {
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
                byte[] queueCountBytes = redisTemplate.opsForValue().get(QUEUE_COUNT_KEY);
                if (queueCountBytes == null) {
                    return null;
                }
                long count = Long.parseLong(new String(queueCountBytes, StandardCharsets.UTF_8));
                for (long queueNumber = 1; queueNumber <= count; queueNumber++) {
                    redisTemplate.delete(getKey(queueNumber));
                }
                redisTemplate.delete(QUEUE_COUNT_KEY);
                redisTemplate.delete(WINNER_SELECTION_DONE_KEY);
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

    @Override
    public void increaseSelectionRunCount() {
        try {
            redisTemplate.opsForValue().increment(WINNER_SELECTION_DONE_KEY);
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
