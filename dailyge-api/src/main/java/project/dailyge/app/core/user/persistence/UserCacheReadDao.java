package project.dailyge.app.core.user.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisException;
import static java.util.Arrays.asList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import project.dailyge.app.common.exception.CommonException;
import static project.dailyge.app.common.script.LuaScript.USER_CACHE_SEARCH_LUA_SCRIPT;
import static project.dailyge.common.configuration.CompressionHelper.decompressAsObjWithZstd;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadRepository;

@Repository
@RequiredArgsConstructor
public class UserCacheReadDao implements UserCacheReadRepository {

    private final RedisTemplate<String, byte[]> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public UserCache findById(final Long userId) {
        try {
            final DefaultRedisScript<byte[]> script = new DefaultRedisScript<>();
            script.setScriptText(USER_CACHE_SEARCH_LUA_SCRIPT);
            script.setResultType(byte[].class);
            final byte[] findCache = redisTemplate.execute(script, asList(getCacheKey(userId), getBlacklistKey(userId)));
            if (findCache == null || findCache.length == 0) {
                return null;
            }
            return decompressAsObjWithZstd(findCache, UserCache.class, objectMapper);
        } catch (RedisException ex) {
            throw CommonException.from(ex.getMessage(), BAD_GATEWAY);
        } catch (Exception ex) {
            throw CommonException.from(ex.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean existsById(final Long userId) {
        try {
            final Boolean hasKey = redisTemplate.hasKey(getCacheKey(userId));
            return Boolean.TRUE.equals(hasKey);
        } catch (RedisException ex) {
            throw CommonException.from(ex.getMessage(), BAD_GATEWAY);
        } catch (Exception ex) {
            throw CommonException.from(ex.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    private static String getCacheKey(Long userId) {
        return String.format("user:cache:%s", userId);
    }

    private static String getBlacklistKey(Long userId) {
        return String.format("user:blacklist:%s", userId);
    }
}
