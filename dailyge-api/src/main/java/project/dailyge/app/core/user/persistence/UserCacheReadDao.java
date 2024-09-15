package project.dailyge.app.core.user.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.RedisSystemException;
import static org.springframework.data.redis.connection.ReturnType.VALUE;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import static project.dailyge.app.common.configuration.script.LuaScript.USER_CACHE_SEARCH_LUA_SCRIPT;
import project.dailyge.app.common.exception.CommonException;
import static project.dailyge.common.configuration.CompressionHelper.decompressAsObjWithZstd;
import static project.dailyge.common.exception.RedisExceptionUtils.resolveRedisSystemException;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadRepository;

@Repository
@RequiredArgsConstructor
public class UserCacheReadDao implements UserCacheReadRepository {

    private final RedisTemplate<String, byte[]> redisTemplate;
    private final String scriptSha;
    private final ObjectMapper objectMapper;

    @Override
    public UserCache findById(final Long userId) {
        try {
            final byte[] findCache = redisTemplate.execute((RedisCallback<byte[]>) connection ->
                connection.scriptingCommands()
                    .evalSha(scriptSha, VALUE, 2, getCacheKey(userId).getBytes(), getBlacklistKey(userId).getBytes()));
            if (findCache == null || findCache.length == 0) {
                return null;
            }
            return decompressAsObjWithZstd(findCache, UserCache.class, objectMapper);
        } catch (RedisSystemException ex) {
            resolveRedisSystemException(ex.getMessage(), getReRegisterScriptExecution());
            throw CommonException.from(ex.getMessage(), BAD_GATEWAY);
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

    private static String getCacheKey(final Long userId) {
        return String.format("user:cache:%s", userId);
    }

    private static String getBlacklistKey(final Long userId) {
        return String.format("user:blacklist:%s", userId);
    }

    private Runnable getReRegisterScriptExecution() {
        try {
            return () -> redisTemplate.execute((RedisCallback<String>) connection ->
                connection.scriptingCommands().scriptLoad(USER_CACHE_SEARCH_LUA_SCRIPT.getBytes())
            );
        } catch (RedisException ex) {
            throw CommonException.from(ex.getMessage(), BAD_GATEWAY);
        } catch (Exception ex) {
            throw CommonException.from(ex.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }
}
