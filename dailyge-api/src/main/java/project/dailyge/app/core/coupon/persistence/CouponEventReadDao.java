package project.dailyge.app.core.coupon.persistence;

import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.core.cache.coupon.CouponCacheReadRepository;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;

@Repository
@RequiredArgsConstructor
public class CouponEventReadDao implements CouponCacheReadRepository {

    private final RedisTemplate<String, byte[]> redisTemplate;
    private static final String COUPON_KEY = "coupon:cache";


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
}
