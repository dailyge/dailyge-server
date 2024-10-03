package project.dailyge.app.core.coupon.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.coupon.exception.CouponTypeException;
import project.dailyge.common.configuration.CompressionHelper;
import project.dailyge.core.cache.coupon.CouponEvent;
import project.dailyge.core.cache.coupon.CouponEventReadRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import static project.dailyge.app.core.coupon.exception.CouponCodeAndMessage.COUPON_KEY_EXCEPTION;

@Repository
@RequiredArgsConstructor
class CouponEventReadDao implements CouponEventReadRepository {

    private final RedisTemplate<String, byte[]> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public boolean existsByUserId(final Long userId, final Long eventId) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForValue().getBit(findEventUserKey(eventId), userId));
        } catch (RedisException exception) {
            throw CommonException.from(exception.getMessage(), BAD_GATEWAY);
        } catch (Exception exception) {
            throw CommonException.from(exception.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<CouponEvent> findBulks(
        final int queueIndex,
        final int limit,
        final Long eventId
    ) {
        final ListOperations<String, byte[]> listOperations = redisTemplate.opsForList();
        final Long size = listOperations.size(findCouponBulkKey(eventId, queueIndex));
        if (size == null || size == 0) {
            return Collections.emptyList();
        }
        final List<byte[]> compressedList = listOperations.range(findCouponBulkKey(eventId, queueIndex), 0, limit - 1);
        return compressedList.stream()
            .map(compressedData -> CompressionHelper.decompressAsObjWithZstd(compressedData, CouponEvent.class, objectMapper))
            .collect(Collectors.toList());
    }

    @Override
    public Integer findQueueCount(final Long eventId) {
        try {
            final byte[] queueCountBytes = redisTemplate.opsForValue().get(findCouponQueueCountKey(eventId));
            if (queueCountBytes == null) {
                return 0;
            }
            try {
                return Integer.parseInt(new String(queueCountBytes, UTF_8));
            } catch (NumberFormatException exception) {
                throw CouponTypeException.from(COUPON_KEY_EXCEPTION);
            }
        } catch (RedisException exception) {
            throw CommonException.from(exception.getMessage(), BAD_GATEWAY);
        } catch (Exception exception) {
            throw CommonException.from(exception.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean isExecuted(final Long eventId) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(findWinnerSelectionKey(eventId)));
        } catch (RedisException exception) {
            throw CommonException.from(exception.getMessage(), BAD_GATEWAY);
        } catch (Exception exception) {
            throw CommonException.from(exception.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    private String findCouponBulkKey(
        final Long eventId,
        final int queueIndex
    ) {
        return String.format("coupon:queue:%s:%s", eventId, queueIndex);
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
