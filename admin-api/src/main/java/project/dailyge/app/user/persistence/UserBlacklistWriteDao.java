package project.dailyge.app.user.persistence;

import io.lettuce.core.RedisException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import project.dailyge.app.common.exception.ExternalServerException;
import project.dailyge.core.cache.user.UserBlacklistWriteRepository;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;

@Repository
@RequiredArgsConstructor
public class UserBlacklistWriteDao implements UserBlacklistWriteRepository {

    private final RedisTemplate<String, byte[]> redisTemplate;

    @Override
    public void save(final String accessToken) {
        executeRedisCommand(() -> {
            redisTemplate.opsForValue().set(String.format("user:blacklist:%s", accessToken), null, Duration.ofHours(1));
        });
    }

    @Override
    public void deleteRefreshToken(final Long userId) {
        executeRedisCommand(() -> {
            redisTemplate.delete(String.format("user:refreshToken:%s", userId));
        });
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
}
