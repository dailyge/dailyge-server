package project.dailyge.core.cache.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayName("[UnitTest] UserCache 단위 테스트")
class UserCacheUnitTest {

    private static final long ID = 1L;
    private static final long ANOTHER_ID = 2L;
    private static final String NICKNAME = "test";
    private static final String EMAIL = "test@gmail.com";
    private static final String PROFILE_IMAGE_URL = "profile.jpg";

    @Test
    @DisplayName("사용자 Cache 를 생성하면, 정상적으로 Cache 가 생성된다.")
    void whenCreateUserCacheThenResultShouldBeNotNull() {
        final UserCache userCache = new UserCache(ID, NICKNAME, EMAIL, PROFILE_IMAGE_URL);

        assertAll(
            () -> assertEquals(ID, userCache.getId()),
            () -> assertEquals(NICKNAME, userCache.getNickname()),
            () -> assertEquals(EMAIL, userCache.getEmail()),
            () -> assertEquals(PROFILE_IMAGE_URL, userCache.getProfileImageUrl())
        );
    }

    @Test
    @DisplayName("사용자 ID가 같다면, 동일한 객체이다.")
    void whenUserIdSameThenInstanceShouldBeSame() {
        final UserCache userCacheWithId1 = new UserCache(ID, NICKNAME, EMAIL, PROFILE_IMAGE_URL);
        final UserCache anotherUserCacheWithId1 = new UserCache(ID, NICKNAME, EMAIL, PROFILE_IMAGE_URL);

        assertEquals(userCacheWithId1, anotherUserCacheWithId1);
    }

    @Test
    @DisplayName("사용자 ID가 다르다면, 다른 객체이다.")
    void whenUserIdDifferentThenInstanceShouldBeDifferent() {
        final UserCache userCacheWithId1 = new UserCache(ID, NICKNAME, EMAIL, PROFILE_IMAGE_URL);
        final UserCache userCacheWithId2 = new UserCache(ANOTHER_ID, NICKNAME, EMAIL, PROFILE_IMAGE_URL);

        assertNotEquals(userCacheWithId1.hashCode(), userCacheWithId2.hashCode());
    }

    @Test
    @DisplayName("사용자 ID가 같다면, 동일한 해시코드를 갖는다.")
    void whenUserIdSameThenHashcodeShouldBeSame() {
        final UserCache userCacheWithId1 = new UserCache(ID, NICKNAME, EMAIL, PROFILE_IMAGE_URL);
        final UserCache anotherUserCacheWithId1 = new UserCache(ID, NICKNAME, EMAIL, PROFILE_IMAGE_URL);

        assertEquals(userCacheWithId1.hashCode(), anotherUserCacheWithId1.hashCode());
    }

    @Test
    @DisplayName("사용자 ID가 다르다면, 다른 해시코드를 갖는다.")
    void whenUserIdDifferentThenHashcodeShouldBeDifferent() {
        final UserCache userCacheWithId1 = new UserCache(ID, NICKNAME, EMAIL, PROFILE_IMAGE_URL);
        final UserCache userCacheWithId2 = new UserCache(ANOTHER_ID, NICKNAME, EMAIL, PROFILE_IMAGE_URL);

        assertNotEquals(userCacheWithId1.hashCode(), userCacheWithId2.hashCode());
    }
}
