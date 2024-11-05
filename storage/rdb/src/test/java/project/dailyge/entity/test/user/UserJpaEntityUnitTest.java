package project.dailyge.entity.test.user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import project.dailyge.entity.user.UserJpaEntity;

@DisplayName("[UnitTest] UserJPAEntity 단위 테스트")
class UserJpaEntityUnitTest {

    private static final String NICKNAME = "nickname";
    private static final String EMAIL = "email@gmail.com";
    private static final String PROFILE_IMAGE_URL = "profileImgUrl.jpg";

    private UserJpaEntity user;

    @BeforeEach
    void setUp() {
        user = new UserJpaEntity(1L, NICKNAME, EMAIL, PROFILE_IMAGE_URL);
    }

    @Test
    @DisplayName("사용자 생성 시, 정상적으로 사용자 인스턴스가 생성된다.")
    void whenUserCreateThenUserShouldBeNotNull() {
        assertAll(
            () -> assertNotNull(user),
            () -> assertEquals(NICKNAME, user.getNickname()),
            () -> assertEquals(EMAIL, user.getEmail()),
            () -> assertEquals(PROFILE_IMAGE_URL, user.getProfileImageUrl())
        );
    }

    @Test
    @DisplayName("사용자 생성 시 프로필 이미지를 넣지 않아도, 정상적으로 사용자 인스턴스가 생성된다.")
    void whenUserCreateByEmptyImageUrlThenUserShouldBeNotNull() {
        final UserJpaEntity userWithoutProfile = new UserJpaEntity(2L, NICKNAME, EMAIL);
        assertAll(
            () -> assertEquals(NICKNAME, userWithoutProfile.getNickname()),
            () -> assertEquals(EMAIL, userWithoutProfile.getEmail()),
            () -> assertNull(userWithoutProfile.getProfileImageUrl())
        );
    }

    @Test
    @DisplayName("허용 가능한 닉네임 길이를 초과하면, IllegalArgumentException이 발생한다.")
    void whenMaxLengthOverNicknameThenIllegalArgumentExceptionShouldBeHappen() {
        final String overLengthNickname = "n".repeat(21);

        assertThatThrownBy(() -> new UserJpaEntity(1L, overLengthNickname, EMAIL))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage(user.getOverMaxNicknameLengthErrorMessage());
    }

    @Test
    @DisplayName("허용 가능한 이메일 길이를 초과하면 IllegalArgumentException이 발생한다.")
    void whenMaxLengthOverEmailThenIllegalArgumentExceptionShouldBeHappen() {
        final String overLengthEmail = "e".repeat(50) + "@domain.com";

        assertThatThrownBy(() -> new UserJpaEntity(1L, NICKNAME, overLengthEmail))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage(user.getInvalidEmailErrorMessage());
    }

    @ParameterizedTest
    @DisplayName("이메일 규격에 맞지 않을 경우 IllegalArgumentException이 발생한다.")
    @ValueSource(strings = {"email.domain.com", "email@damain,com", "email@domain@domain.com"})
    void whenNotCompliantEmailThenIllegalArgumentExceptionShouldBeHappen(final String notCompliantEmail) {
        assertThatThrownBy(() -> new UserJpaEntity(1L, NICKNAME, notCompliantEmail))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage(user.getInvalidEmailErrorMessage());
    }

    @Test
    @DisplayName("허용 가능한 프로필 이미지 URL 길이를 초과하면 IllegalArgumentException이 발생한다.")
    void whenMaxLengthOverProfileImageUrlThenIllegalArgumentExceptionShouldBeHappen() {
        final String overLengthProfileImageUrl = "p".repeat(2001);

        assertThatThrownBy(() -> new UserJpaEntity(1L, NICKNAME, EMAIL, overLengthProfileImageUrl))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage(user.getOverMaxProfileImageUrlErrorMessage());
    }

    @Test
    @DisplayName("이미 삭제 된 사용자를 삭제할 경우, IllegalArgumentException이 발생한다.")
    void whenDeleteAnAlreadyDeletedUserThenIllegalArgumentExceptionShouldBeHappen() {
        user.delete();

        assertThatThrownBy(() -> user.delete())
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage(user.getUserAlreadyDeletedMessage());
    }

    @Test
    @DisplayName("사용자 ID가 같다면, 동일한 객체이다.")
    void whenUserIdSameThenInstanceShouldBeSame() {
        final UserJpaEntity userWithId1 = new UserJpaEntity(1L, NICKNAME, EMAIL);
        final UserJpaEntity anotherUserWithId1 = new UserJpaEntity(1L, NICKNAME, EMAIL);

        assertEquals(userWithId1, anotherUserWithId1);
    }

    @Test
    @DisplayName("사용자 ID가 다르다면, 다른 객체이다.")
    void whenUserIdDifferentThenInstanceShouldBeDifferent() {
        final UserJpaEntity userWithId1 = new UserJpaEntity(1L, NICKNAME, EMAIL);
        final UserJpaEntity userWithId2 = new UserJpaEntity(2L, NICKNAME, EMAIL);

        assertNotEquals(userWithId1, userWithId2);
    }

    @Test
    @DisplayName("사용자 ID가 같다면, 해시코드가 동일하다.")
    void whenUserIdSameThenHashcodeShouldBeSame() {
        final UserJpaEntity userWithId1 = new UserJpaEntity(1L, NICKNAME, EMAIL);
        final UserJpaEntity anotherUserWithId1 = new UserJpaEntity(1L, NICKNAME, EMAIL);

        assertEquals(userWithId1.hashCode(), anotherUserWithId1.hashCode());
    }

    @Test
    @DisplayName("사용자 ID가 다르다면, 해시코드는 다른 값이다.")
    void whenUserIdDifferentThenHashcodeShouldBeDifferent() {
        final UserJpaEntity userWithId1 = new UserJpaEntity(1L, NICKNAME, EMAIL);
        final UserJpaEntity userWithId2 = new UserJpaEntity(2L, NICKNAME, EMAIL);

        assertNotEquals(userWithId1.hashCode(), userWithId2.hashCode());
    }
}
