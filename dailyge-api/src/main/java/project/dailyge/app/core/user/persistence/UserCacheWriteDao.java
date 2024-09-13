package project.dailyge.app.core.user.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisException;
import static java.time.Duration.ofDays;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import project.dailyge.app.common.exception.CommonException;
import static project.dailyge.common.configuration.CompressionHelper.compressAsByteArrayWithZstd;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheWriteRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserCacheWriteDao implements UserCacheWriteRepository {

    private static final long CACHE_DURATION = 90;

    private final RedisTemplate<String, byte[]> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void save(final UserCache userCache) {
        final byte[] compressedCache = compressAsByteArrayWithZstd(userCache, objectMapper);
        executeRedisCommand(() ->
            redisTemplate.opsForValue().set(
                getUserCacheKey(userCache.getId()),
                compressedCache,
                ofDays(CACHE_DURATION)
            )
        );
    }

    @Override
    public void refreshExpirationDate(final Long userId) {
        executeRedisCommand(() -> redisTemplate.expire(getUserCacheKey(userId), ofDays(CACHE_DURATION)));
    }

    @Override
    public void delete(final Long userId) {
        executeRedisCommand(() -> redisTemplate.delete(getUserCacheKey(userId)));
    }

    /**
     * 성능 측정을 위한 메서드로, 외부에서 호출하지 말 것.
     */
    public void saveUserCacheBulk(final List<UserCache> userCaches) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (final UserCache userCache : userCaches) {
                final byte[] compressedCache = compressAsByteArrayWithZstd(userCache, objectMapper);
                final String key = getUserCacheKey(userCache.getId());
                connection.set(key.getBytes(), compressedCache);
                connection.expire(key.getBytes(), CACHE_DURATION * 24 * 60 * 60);
            }
            return null;
        });
    }

    /**
     * 성능 측정을 위한 메서드로, 외부에서 호출하지 말 것.
     */
    public void saveBlackListCacheBulk(final List<UserCache> blackListCache) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (final UserCache userCache : blackListCache) {
                final byte[] compressedCache = compressAsByteArrayWithZstd(userCache, objectMapper);
                final String key = getBlackListKey(userCache.getId());
                connection.set(key.getBytes(), compressedCache);
                connection.expire(key.getBytes(), CACHE_DURATION * 24 * 60 * 60);
            }
            return null;
        });
    }

    private void executeRedisCommand(final Runnable command) {
        try {
            command.run();
        } catch (RedisException ex) {
            throw CommonException.from(ex.getMessage(), BAD_GATEWAY);
        } catch (Exception ex) {
            throw CommonException.from(ex.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    private String getUserCacheKey(final Long userId) {
        return String.format("user:cache:%s", userId);
    }

    private String getBlackListKey(final Long userId) {
        return String.format("user:blacklist:%s", userId);
    }
}
