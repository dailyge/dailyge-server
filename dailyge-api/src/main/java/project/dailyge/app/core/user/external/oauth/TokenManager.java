package project.dailyge.app.core.user.external.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import project.dailyge.app.common.codeandmessage.CommonCodeAndMessage;
import project.dailyge.app.common.exception.ExternalServerException;

import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.common.exception.ExternalServerException.REDIS_SAVE_FAILED_MESSAGE;
import static project.dailyge.app.common.exception.ExternalServerException.REDIS_SEARCH_FAILED_MESSAGE;

@Service
@RequiredArgsConstructor
public class TokenManager {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveRefreshToken(
        final Long userId,
        final String refreshToken
    ) {
        try {
            redisTemplate.opsForValue().set(userId.toString(), refreshToken);
        } catch (Exception ex) {
            throw new ExternalServerException(REDIS_SAVE_FAILED_MESSAGE, BAD_GATEWAY);
        }
    }

    private String getRefreshTokenKey(final Long userId) {
        try {
            return redisTemplate.opsForValue().get(userId.toString());
        } catch (Exception ex) {
            throw new ExternalServerException(REDIS_SEARCH_FAILED_MESSAGE, BAD_GATEWAY);
        }
    }
}
