package project.dailyge.app.core.user.external.oauth;

import io.netty.handler.timeout.TimeoutException;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.SerializationException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import project.dailyge.app.common.exception.ExternalServerException;
import project.dailyge.app.core.user.exception.UserTypeException;

import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.*;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.EMPTY_USER_ID;

@Service
@RequiredArgsConstructor
public class TokenManager {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveRefreshToken(
        final Long userId,
        final String refreshToken
    ) {
        if (userId == null) {
            throw UserTypeException.from(EMPTY_USER_ID);
        }
        try {
            redisTemplate.opsForValue().set(String.format("user:refreshToken:%s", userId), refreshToken);
        } catch (IllegalArgumentException ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_REQUEST);
        } catch (RedisConnectionFailureException ex) {
            throw new ExternalServerException(ex.getMessage(), SERVICE_UNAVAILABLE);
        } catch (TimeoutException ex) {
            throw new ExternalServerException(ex.getMessage(), GATEWAY_TIMEOUT);
        } catch (SerializationException ex) {
            throw new ExternalServerException(ex.getMessage(), INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_GATEWAY);
        }
    }

    public String getRefreshTokenKey(final Long userId) {
        if (userId == null) {
            throw UserTypeException.from(EMPTY_USER_ID);
        }
        try {
            return redisTemplate.opsForValue().get(String.format("user:refreshToken:%s", userId));
        } catch (RedisConnectionFailureException ex) {
            throw new ExternalServerException(ex.getMessage(), SERVICE_UNAVAILABLE);
        } catch (TimeoutException ex) {
            throw new ExternalServerException(ex.getMessage(), GATEWAY_TIMEOUT);
        } catch (IllegalArgumentException ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_REQUEST);
        } catch (SerializationException ex) {
            throw new ExternalServerException(ex.getMessage(), INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_GATEWAY);
        }
    }
}
