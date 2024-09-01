package project.dailyge.app.core.common.web;

import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.core.cache.user.UserBlacklistReadRepository;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;

@Repository
@RequiredArgsConstructor
public class UserBlacklistReadDao implements UserBlacklistReadRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean existsByAccessToken(final String accessToken) {
        try {
            return redisTemplate.hasKey(String.format("user:blacklist:%s", accessToken));
        } catch (RedisException ex) {
            throw CommonException.from(ex.getMessage(), BAD_GATEWAY);
        } catch (Exception ex) {
            throw CommonException.from(ex.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }
}
