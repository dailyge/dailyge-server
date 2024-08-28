package project.dailyge.app.user.persistence;

import io.lettuce.core.RedisException;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import project.dailyge.app.common.exception.ExternalServerException;
import project.dailyge.core.cache.user.UserBlacklistWriteRepository;
import static java.nio.charset.StandardCharsets.UTF_8;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import static project.dailyge.common.configuration.CompressionHelper.compressStringAsByteArray;

@Repository
@RequiredArgsConstructor
public class UserBlacklistWriteDao implements UserBlacklistWriteRepository {

    private final RedisTemplate<String, byte[]> redisTemplate;

    @Override
    public void save(final String accessToken) {
        executeRedisCommand(() -> {
            final byte[] compressedRefreshToken = compressStringAsByteArray(accessToken.getBytes(UTF_8));
            redisTemplate.opsForSet().add("user:blacklist", compressedRefreshToken);
            return null;
        });
    }

    @Override
    public void deleteRefreshToken(final Long userId) {
        executeRedisCommand(() -> {
            redisTemplate.delete(String.format("user:refreshToken:%s", userId));
            return null;
        });
    }

    private String executeRedisCommand(final Supplier<String> command) {
        try {
            return command.get();
        } catch (RedisException ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_GATEWAY);
        } catch (Exception ex) {
            throw new ExternalServerException(ex.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }
}
