package project.dailyge.app.core.user.external.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import project.dailyge.app.common.exception.ExternalServerException;

import java.util.function.Supplier;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;

@Service
@RequiredArgsConstructor
public class TokenManager {

    private final StringRedisTemplate redisTemplate;

    public void saveRefreshToken(
        final Long userId,
        final String refreshToken
    ) {
        executeRedisCommand(() -> {
            redisTemplate.opsForValue().set(String.format("user:refreshToken:%s", userId), refreshToken);
            return null;
        });
    }

    public String getRefreshToken(final Long userId) {
        return executeRedisCommand(() ->
            redisTemplate.opsForValue().get(String.format("user:refreshToken:%s", userId))
        );
    }

    public void deleteRefreshToken(final Long userId) {
        executeRedisCommand(() -> {
            redisTemplate.delete(String.format("user:refreshToken:%s", userId));
            return null;
        });
    }

    private String executeRedisCommand(final Supplier<String> command) {
        try {
            return command.get();
        } catch (DataAccessException ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_GATEWAY);
        } catch (Exception ex) {
            throw new ExternalServerException(ex.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }
}
