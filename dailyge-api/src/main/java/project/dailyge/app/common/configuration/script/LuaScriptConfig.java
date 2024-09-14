package project.dailyge.app.common.configuration.script;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import static project.dailyge.app.common.configuration.script.LuaScript.USER_CACHE_SEARCH_LUA_SCRIPT;

@Configuration
public class LuaScriptConfig {

    @Bean
    public DefaultRedisScript<byte[]> userCacheSearchScript() {
        final DefaultRedisScript<byte[]> script = new DefaultRedisScript<>();
        script.setScriptText(USER_CACHE_SEARCH_LUA_SCRIPT);
        script.setResultType(byte[].class);
        return script;
    }
}
