package project.dailyge.app.core.user.external.oauth;

import io.lettuce.core.RedisException;
import static java.nio.charset.StandardCharsets.UTF_8;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import project.dailyge.app.common.exception.ExternalServerException;
import static project.dailyge.common.configuration.CompressionHelper.compressStringAsByteArray;
import static project.dailyge.common.configuration.CompressionHelper.decompressAsString;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class TokenManager {

    private final RedisTemplate<String, byte[]> redisTemplate;

    public void saveRefreshToken(
        final Long userId,
        final String refreshToken
    ) {
        try {
            executeRedisCommand(() -> {
                final byte[] compressedRefreshToken = compressStringAsByteArray(refreshToken.getBytes(UTF_8));
                redisTemplate.opsForValue().set(getKey(userId), compressedRefreshToken);
                return null;
            });
        } catch (RedisException ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_GATEWAY);
        } catch (Exception ex) {
            throw new ExternalServerException(ex.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    public String getRefreshToken(final Long userId) {
        try {
            return executeRedisCommand(() -> {
                final byte[] refreshToken = redisTemplate.opsForValue().get(getKey(userId));
                if (refreshToken == null) {
                    return null;
                }
                return decompressAsString(refreshToken);
            });
        } catch (RedisException ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_GATEWAY);
        } catch (Exception ex) {
            throw new ExternalServerException(ex.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteRefreshToken(final Long userId) {
        try {
            executeRedisCommand(() -> {
                redisTemplate.delete(getKey(userId));
                return null;
            });
        } catch (RedisException ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_GATEWAY);
        } catch (Exception ex) {
            throw new ExternalServerException(ex.getMessage(), INTERNAL_SERVER_ERROR);
        }
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

    private static String getKey(Long userId) {
        return String.format("user:refreshToken:%s", userId);
    }
}
