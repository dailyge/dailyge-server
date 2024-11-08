package project.dailyge.app.core.user.external.oauth;

import io.lettuce.core.RedisException;
import org.springframework.data.redis.core.RedisTemplate;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import project.dailyge.app.common.annotation.ExternalLayer;
import project.dailyge.app.common.exception.CommonException;

import java.util.function.Supplier;

@ExternalLayer(value = "TokenManager")
public class TokenManager {

    private final RedisTemplate<String, String> redisTemplate;

    public TokenManager(final RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveRefreshToken(
        final Long userId,
        final String refreshToken
    ) {
        executeRedisCommand(() -> {
            redisTemplate.opsForValue().set(getKey(userId), refreshToken);
            return null;
        });
    }

    public String getRefreshToken(final Long userId) {
        return executeRedisCommand(() -> {
            return redisTemplate.opsForValue().get(getKey(userId));
        });
    }

    public void deleteRefreshToken(final Long userId) {
        executeRedisCommand(() -> {
            redisTemplate.delete(getKey(userId));
            return null;
        });
    }

    private String executeRedisCommand(final Supplier<String> command) {
        try {
            return command.get();
        } catch (RedisException ex) {
            throw CommonException.from(ex.getMessage(), BAD_GATEWAY);
        } catch (Exception ex) {
            throw CommonException.from(ex.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    private static String getKey(Long userId) {
        return String.format("user:refreshToken:%s", userId);
    }
}
