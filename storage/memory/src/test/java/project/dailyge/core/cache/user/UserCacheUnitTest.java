package project.dailyge.core.cache.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayName("[UnitTest] UserCache 단위 테스트")
class UserCacheUnitTest {

    private static final long ID = 1L;
    private static final long ANOTHER_ID = 2L;
    private static final String NICKNAME = "dailyges";
    private static final String EMAIL = "dailyges@gmail.com";
    private static final String PROFILE_IMAGE_URL = "profile.jpg";
    private static final String ROLE = "NORMAL";

    private static UserCache userCache;
    private static UserCache userCacheWithId1;
    private static UserCache userCacheWithId2;

    @BeforeAll
    static void setUp() {
        userCache = new UserCache(
            ID,
            NICKNAME,
            EMAIL,
            PROFILE_IMAGE_URL,
            ROLE
        );
        userCacheWithId1 = new UserCache(
            ID,
            NICKNAME,
            EMAIL,
            PROFILE_IMAGE_URL,
            ROLE
        );
        userCacheWithId2 = new UserCache(
            ANOTHER_ID,
            NICKNAME,
            EMAIL,
            PROFILE_IMAGE_URL,
            ROLE
        );
    }

    @Test
    @DisplayName("사용자 Cache 를 생성하면, 정상적으로 Cache 가 생성된다.")
    void whenCreateUserCacheThenResultShouldBeNotNull() {
        assertAll(
            () -> assertEquals(ID, userCache.getId()),
            () -> assertEquals(NICKNAME, userCache.getNickname()),
            () -> assertEquals(EMAIL, userCache.getEmail()),
            () -> assertEquals(PROFILE_IMAGE_URL, userCache.getProfileImageUrl()),
            () -> assertEquals(ROLE, userCache.getRole())
        );
    }

    @Test
    @DisplayName("사용자 ID가 같다면, 동일한 객체이다.")
    void whenUserIdSameThenInstanceShouldBeSame() {
        assertEquals(userCache, userCacheWithId1);
    }

    @Test
    @DisplayName("사용자 ID가 다르다면, 다른 객체이다.")
    void whenUserIdDifferentThenInstanceShouldBeDifferent() {
        assertNotEquals(userCache.hashCode(), userCacheWithId2.hashCode());
    }

    @Test
    @DisplayName("사용자 ID가 같다면, 동일한 해시코드를 갖는다.")
    void whenUserIdSameThenHashcodeShouldBeSame() {
        assertEquals(userCache.hashCode(), userCacheWithId1.hashCode());
    }

    @Test
    @DisplayName("사용자 ID가 다르다면, 다른 해시코드를 갖는다.")
    void whenUserIdDifferentThenHashcodeShouldBeDifferent() {
        assertNotEquals(userCache.hashCode(), userCacheWithId2.hashCode());
    }
}
