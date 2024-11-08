package project.dailyge.app.core.user.persistence;

import io.lettuce.core.RedisException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.core.cache.user.UserBlacklistWriteRepository;

import java.time.Duration;

@Repository
public class UserBlacklistWriteDao implements UserBlacklistWriteRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public UserBlacklistWriteDao(final RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(final String accessToken) {
        executeRedisCommand(() -> redisTemplate.opsForValue().set(String.format("user:blacklist:%s", accessToken), "", Duration.ofHours(1)));
    }

    @Override
    public void deleteRefreshToken(final Long userId) {
        executeRedisCommand(() -> redisTemplate.delete(String.format("user:refreshToken:%s", userId)));
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
}
