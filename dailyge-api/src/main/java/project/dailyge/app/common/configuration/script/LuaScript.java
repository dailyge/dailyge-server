package project.dailyge.app.common.configuration.script;

public interface LuaScript {
    String USER_CACHE_SEARCH_LUA_SCRIPT =
        "local cache_key = KEYS[1] "
            + "local blacklist_key = KEYS[2] "
            + "local cache = redis.call('GET', cache_key) "
            + "if not cache then "
            + "    return nil "
            + "end "
            + "if redis.call('EXISTS', blacklist_key) == 1 then "
            + "    return nil "
            + "end "
            + "return cache ";
}
