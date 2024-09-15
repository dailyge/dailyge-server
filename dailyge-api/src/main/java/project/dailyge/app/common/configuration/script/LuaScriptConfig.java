package project.dailyge.app.common.configuration.script;

import io.lettuce.core.RedisException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;
import static project.dailyge.app.common.configuration.script.LuaScript.USER_CACHE_SEARCH_LUA_SCRIPT;
import project.dailyge.app.common.exception.CommonException;

@Configuration
public class LuaScriptConfig {

    @Bean
    public String scriptSha(final RedisTemplate<String, byte[]> redisTemplate) {
        try {
            return redisTemplate.execute((RedisCallback<String>) connection ->
                connection.scriptingCommands().scriptLoad(USER_CACHE_SEARCH_LUA_SCRIPT.getBytes())
            );
        } catch (RedisException ex) {
            throw CommonException.from(ex.getMessage(), BAD_GATEWAY);
        } catch (Exception ex) {
            throw CommonException.from(ex.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }
}
