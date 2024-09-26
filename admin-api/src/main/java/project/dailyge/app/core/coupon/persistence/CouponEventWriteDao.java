package project.dailyge.app.core.coupon.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.common.configuration.CompressionHelper;
import project.dailyge.core.cache.coupon.CouponEvent;
import project.dailyge.core.cache.coupon.CouponEventWriteRepository;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;

@Repository
@RequiredArgsConstructor
public class CouponEventWriteDao implements CouponEventWriteRepository {

    private final RedisTemplate<String, byte[]> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void saveBulks(
        final List<CouponEvent> couponCaches,
        final Long eventId
    ) {
        if (couponCaches == null || couponCaches.isEmpty()) {
            return;
        }
        final List<byte[]> compressedCouponEvents = couponCaches.stream()
            .map(data -> CompressionHelper.compressAsByteArrayWithZstd(data, objectMapper))
            .toList();
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            couponCaches.forEach(couponCache -> redisTemplate.opsForValue().setBit(findEventUserKey(eventId), couponCache.getUserId(), true));
            final Long newQueueIndex = redisTemplate.opsForValue().increment(findCouponQueueCountKey(eventId));
            byte[] keyBytes = redisTemplate.getStringSerializer().serialize(findCouponBulkKey(eventId, newQueueIndex));
            connection.listCommands().rPush(keyBytes, compressedCouponEvents.toArray(new byte[0][]));
            return null;
        });
    }

    @Override
    public void deleteAllBulks(final Long eventId) {
        try {
            redisTemplate.execute(connection -> {
                connection.openPipeline();
                final byte[] queueCountBytes = redisTemplate.opsForValue().get(findCouponQueueCountKey(eventId));
                if (queueCountBytes == null) {
                    return null;
                }
                final long count = Long.parseLong(new String(queueCountBytes, UTF_8));
                for (long queueNumber = 1; queueNumber <= count; queueNumber++) {
                    redisTemplate.delete(findCouponBulkKey(eventId, queueNumber));
                }
                redisTemplate.delete(findCouponQueueCountKey(eventId));
                redisTemplate.delete(findWinnerSelectionKey(eventId));
                redisTemplate.opsForValue().set(findEventUserKey(eventId), "\0".getBytes());
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
    public void saveEventRun(final Long eventId) {
        try {
            redisTemplate.opsForValue().increment(findWinnerSelectionKey(eventId));
        } catch (RedisException exception) {
            throw CommonException.from(exception.getMessage(), BAD_GATEWAY);
        } catch (Exception exception) {
            throw CommonException.from(exception.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    private String findCouponBulkKey(
        final Long eventId,
        final Long queueNumber
    ) {
        return String.format("coupon:queue:%s:%s", eventId, queueNumber);
    }

    private String findCouponQueueCountKey(final Long eventId) {
        return String.format("coupon:queue:count:%s", eventId);
    }

    private String findWinnerSelectionKey(final Long eventId) {
        return String.format("coupon:winner:%s", eventId);
    }

    private String findEventUserKey(final Long eventId) {
        return String.format("event:user:%s", eventId);
    }
}
