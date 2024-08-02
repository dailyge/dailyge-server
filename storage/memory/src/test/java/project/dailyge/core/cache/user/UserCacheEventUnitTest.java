package project.dailyge.core.cache.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("[UnitTest] UserCacheEvent 단위 테스트")
class UserCacheEventUnitTest {

    @Test
    @DisplayName("UserCacheEvent 를 생성하면, 정상적으로 생성된다.")
    void whenCreateUserCacheEventThenResultShouldBeNotNull() {
        final UserCache userCache = new UserCache(1L, "test", "test@gmail.com", "profile.jpg");
        final UserCacheEvent userCacheEvent = new UserCacheEvent(userCache);

        assertEquals(userCache, userCacheEvent.userCache());
    }
}
