package project.dailyge.app.coupon.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.core.cache.coupon.CouponEvent;
import project.dailyge.core.cache.coupon.CouponEventReadRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import static project.dailyge.common.configuration.CompressionHelper.decompressAsObjWithZstd;

@Repository
@RequiredArgsConstructor
class CouponEventReadDao implements CouponEventReadRepository {

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
    public List<CouponEvent> findBulks(final int queueNumber) {
        final CouponEventBulks couponEventBulks = decompressAsObjWithZstd(
            Objects.requireNonNull(redisTemplate.opsForValue().get(getKey(queueNumber))),
            CouponEventBulks.class, objectMapper);
        return couponEventBulks.couponCaches();
    }

    @Override
    public List<CouponEvent> findBulksByLimit(final int queueNumber, final int limit) {
        return findBulks(queueNumber).subList(0, limit);
    }

    @Override
    public Integer findQueueCount() {
        try {
            byte[] queueCountBytes = redisTemplate.opsForValue().get(QUEUE_COUNT_KEY);
            if (queueCountBytes == null) {
                return 0;
            }
            return Integer.parseInt(new String(queueCountBytes, StandardCharsets.UTF_8));
        } catch (RedisException exception) {
            throw CommonException.from(exception.getMessage(), BAD_GATEWAY);
        } catch (Exception exception) {
            throw CommonException.from(exception.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean hasSelectionRun() {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(WINNER_SELECTION_DONE_KEY));
        } catch (RedisException exception) {
            throw CommonException.from(exception.getMessage(), BAD_GATEWAY);
        } catch (Exception exception) {
            throw CommonException.from(exception.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    private static String getKey(final int queueNumber) {
        return String.format("coupon:queue:%s", queueNumber);
    }
}
