package project.dailyge.core.cache.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("[UnitTest] UserCache 단위 테스트")
class UserCacheUnitTest {

    @Test
    @DisplayName("사용자 Cache 를 생성하면, 정상적으로 Cache 가 생성된다.")
    void whenCreateUserCacheThenResultShouldBeNotNull() {
        final Long id = 1L;
        final String nickname = "test";
        final String email = "test@gmail.com";
        final String profileImage = "profile.jpg";
        final UserCache userCache = new UserCache(id, nickname, email, profileImage);

        assertAll(
            () -> assertEquals(id, userCache.getId()),
            () -> assertEquals(nickname, userCache.getNickname()),
            () -> assertEquals(email, userCache.getEmail()),
            () -> assertEquals(profileImage, userCache.getProfileImageUrl())
        );
    }
}
