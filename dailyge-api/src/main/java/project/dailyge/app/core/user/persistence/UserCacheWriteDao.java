package project.dailyge.app.core.user.persistence;

import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import project.dailyge.app.common.exception.ExternalServerException;
import static project.dailyge.common.configuration.CompressionHelper.compressAsByteArray;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheWriteRepository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class UserCacheWriteDao implements UserCacheWriteRepository {

    private final RedisTemplate<String, byte[]> redisTemplate;

    @Override
    public void save(final UserCache userCache) {
        try {
            final byte[] compressedCache = compressAsByteArray(userCache);
            redisTemplate.opsForValue().set(
                getKey(userCache.getId()),
                compressedCache,
                Duration.ofDays(90)
            );
        } catch (RedisException ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_GATEWAY);
        } catch (Exception ex) {
            throw new ExternalServerException(ex.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void refreshExpirationDate(final Long userId) {
        try {
            redisTemplate.expire(getKey(userId), Duration.ofDays(90));
        } catch (RedisException ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_GATEWAY);
        } catch (Exception ex) {
            throw new ExternalServerException(ex.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    private String getKey(final Long userId) {
        return String.format("user:cache:%s", userId);
    }
}
