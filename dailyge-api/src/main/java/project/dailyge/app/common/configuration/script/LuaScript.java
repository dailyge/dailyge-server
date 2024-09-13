package project.dailyge.app.common.configuration.script;

public interface LuaScript {
    String USER_CACHE_SEARCH_LUA_SCRIPT =
        "local cache_key = KEYS[1] " +
            "local blacklist_key = KEYS[2] " +
            "local cache = redis.call('GET', cache_key) " +
            "if cache then " +
            "    local is_blacklisted = redis.call('EXISTS', blacklist_key) " +
            "    if is_blacklisted == 1 then " +
            "        return nil " +
            "    else " +
            "        return cache " +
            "    end " +
            "else " +
            "    return nil " +
            "end";
}
