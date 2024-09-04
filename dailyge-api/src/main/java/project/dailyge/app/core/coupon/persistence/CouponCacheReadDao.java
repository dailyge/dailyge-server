package project.dailyge.app.core.coupon.persistence;

import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.core.cache.coupon.CouponCacheReadRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import static project.dailyge.common.configuration.CompressionHelper.decompressAsObj;

@Repository
@RequiredArgsConstructor
public class CouponCacheReadDao implements CouponCacheReadRepository {
    private final RedisTemplate<String, byte[]> redisTemplate;
    private static final String COUPON_KEY_PREFIX = "coupon_apply:";
    private static final String COUPON_COUNT_KEY = "coupon_count";


    @Override
    public boolean existsByUserId(final Long userId) {
        Set<byte[]> elements = findByUserId(userId);
        if (elements == null) {
            return false;
        }
        return elements.isEmpty();
    }

    private Set<byte[]> findByUserId(final Long userId) {
        try {
            Integer count = decompressAsObj(redisTemplate.opsForValue().get(COUPON_COUNT_KEY), Integer.class);
            if (count == null || count <= 0) {
                return null;
            }
            List<String> keys = IntStream.rangeClosed(1, count)
                .mapToObj(i -> COUPON_KEY_PREFIX + i)
                .toList();
            for (String key : keys) {
                Set<byte[]> elements = redisTemplate.opsForZSet().rangeByScore(key, userId, userId);
                if (elements != null && elements.isEmpty()) {
                    return elements;
                }
            }
            return null;
        } catch (RedisException exception) {
            throw CommonException.from(exception.getMessage(), BAD_GATEWAY);
        } catch (Exception exception) {
            throw CommonException.from(exception.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }


}
