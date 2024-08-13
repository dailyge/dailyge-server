package project.dailyge.app.core.user.persistence;

import io.lettuce.core.RedisException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import project.dailyge.app.common.exception.ExternalServerException;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheWriteRepository;
import static java.time.Duration.ofDays;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import static project.dailyge.common.configuration.CompressionHelper.compressAsByteArray;

@Repository
@RequiredArgsConstructor
public class UserCacheWriteDao implements UserCacheWriteRepository {

    private static final long CACHE_DURATION = 90;

    private final RedisTemplate<String, byte[]> redisTemplate;

    @Override
    public void save(final UserCache userCache) {
        final byte[] compressedCache = compressAsByteArray(userCache);
        executeRedisCommand(() ->
            redisTemplate.opsForValue().set(
                getKey(userCache.getId()),
                compressedCache,
                Duration.ofDays(90)
            )
        );
    }

    @Override
    public void refreshExpirationDate(final Long userId) {
        executeRedisCommand(() -> redisTemplate.expire(getKey(userId), ofDays(CACHE_DURATION)));
    }

    @Override
    public void delete(final Long userId) {
        executeRedisCommand(() -> redisTemplate.delete(getKey(userId)));
    }

    private void executeRedisCommand(final Runnable command) {
        try {
            command.run();
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
