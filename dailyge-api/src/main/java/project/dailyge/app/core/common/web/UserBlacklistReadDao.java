package project.dailyge.app.core.common.web;

import io.lettuce.core.RedisException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import project.dailyge.app.common.exception.ExternalServerException;
import project.dailyge.core.cache.user.UserBlacklistReadRepository;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import static project.dailyge.common.configuration.CompressionHelper.decompressAsObj;

@Component
@RequiredArgsConstructor
public class UserBlacklistReadDao implements UserBlacklistReadRepository {

    private final RedisTemplate<String, byte[]> redisTemplate;

    public Date getBlacklistById(final Long userId) {
        try {
            final byte[] findBlacklistSaveTime = redisTemplate.opsForValue().get(String.format("user:blacklist:%s", userId));
            if (findBlacklistSaveTime == null) {
                return null;
            }
            return decompressAsObj(findBlacklistSaveTime, Date.class);
        } catch (RedisException ex) {
            throw new ExternalServerException(ex.getMessage(), BAD_GATEWAY);
        } catch (Exception ex) {
            throw new ExternalServerException(ex.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }
}
