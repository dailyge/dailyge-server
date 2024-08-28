package project.dailyge.app.user.persistence;

import io.lettuce.core.RedisException;
import java.util.Date;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import project.dailyge.app.common.exception.ExternalServerException;
import project.dailyge.core.cache.user.UserBlacklistWriteRepository;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import static project.dailyge.common.configuration.CompressionHelper.compressAsByteArray;

@Repository
@RequiredArgsConstructor
public class UserBlacklistWriteDao implements UserBlacklistWriteRepository {

    private final RedisTemplate<String, byte[]> redisTemplate;

    @Override
    public void saveBlacklistById(
        final Long userId
    ) {
        executeRedisCommand(() -> {
            final Date date = new Date();
            final byte[] compressedRefreshToken = compressAsByteArray(date);
            redisTemplate.opsForValue().set(getBlacklistKey(userId), compressedRefreshToken);
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

    private static String getBlacklistKey(final Long userId) {
        return String.format("user:blacklist:%s", userId);
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
