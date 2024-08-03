package project.dailyge.app.core.user.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.timeout.TimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import project.dailyge.app.common.exception.ExternalServerException;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadRepository;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_REQUEST;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.GATEWAY_TIMEOUT;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.SERVICE_UNAVAILABLE;

@Repository
@RequiredArgsConstructor
public class UserCacheReadDao implements UserCacheReadRepository {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public UserCache findById(final Long userId) {
        try {
            final String userCacheJson = redisTemplate.opsForValue().get(String.format("user:cache:%s", userId));
            return objectMapper.readValue(userCacheJson, UserCache.class);
        } catch (IllegalArgumentException ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_REQUEST);
        } catch (RedisConnectionFailureException ex) {
            throw new ExternalServerException(ex.getMessage(), SERVICE_UNAVAILABLE);
        } catch (TimeoutException ex) {
            throw new ExternalServerException(ex.getMessage(), GATEWAY_TIMEOUT);
        } catch (JsonProcessingException ex) {
            throw new ExternalServerException(ex.getMessage(), INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_GATEWAY);
        }
    }
}
