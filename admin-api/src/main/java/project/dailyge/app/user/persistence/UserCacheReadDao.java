package project.dailyge.app.user.persistence;

import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import project.dailyge.app.common.exception.ExternalServerException;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadRepository;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import static project.dailyge.common.configuration.CompressionHelper.decompressAsObj;

@Repository
@RequiredArgsConstructor
public class UserCacheReadDao implements UserCacheReadRepository {

    private final RedisTemplate<String, byte[]> redisTemplate;

    @Override
    public UserCache findById(final Long userId) {
        try {
            final byte[] cache = redisTemplate.opsForValue().get(getKey(userId));
            if (cache == null) {
                return null;
            }
            return decompressAsObj(cache, UserCache.class);
        } catch (RedisException ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_GATEWAY);
        } catch (Exception ex) {
            throw new ExternalServerException(ex.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean existsById(final Long userId) {
        try {
            final Boolean hasKey = redisTemplate.hasKey(getKey(userId));
            return Boolean.TRUE.equals(hasKey);
        } catch (RedisException ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_GATEWAY);
        } catch (Exception ex) {
            throw new ExternalServerException(ex.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    private static String getKey(Long userId) {
        return String.format("user:cache:%s", userId);
    }
}
