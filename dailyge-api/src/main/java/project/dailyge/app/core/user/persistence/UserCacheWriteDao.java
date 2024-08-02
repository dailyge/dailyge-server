package project.dailyge.app.core.user.persistence;

import io.netty.handler.timeout.TimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import project.dailyge.app.common.exception.ExternalServerException;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheWriteRepository;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_REQUEST;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.GATEWAY_TIMEOUT;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.SERVICE_UNAVAILABLE;

@Repository
@RequiredArgsConstructor
public class UserCacheWriteDao implements UserCacheWriteRepository {

    private final RedisTemplate<String, UserCache> redisTemplate;

    @Override
    public void save(final UserCache userCache) {
        try {
            redisTemplate.opsForValue().set(String.format("user:cache:%s", userCache.getId()), userCache);
        } catch (IllegalArgumentException ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_REQUEST);
        } catch (RedisConnectionFailureException ex) {
            throw new ExternalServerException(ex.getMessage(), SERVICE_UNAVAILABLE);
        } catch (TimeoutException ex) {
            throw new ExternalServerException(ex.getMessage(), GATEWAY_TIMEOUT);
        } catch (Exception ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_GATEWAY);
        }
    }
}
