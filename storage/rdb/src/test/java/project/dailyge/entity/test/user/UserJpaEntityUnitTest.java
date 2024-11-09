package project.dailyge.entity.test.user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static project.dailyge.entity.user.Role.NORMAL;
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
            () -> assertEquals(PROFILE_IMAGE_URL, user.getProfileImageUrl()),
            () -> assertFalse(user.isBlacklist())
        );
    }

    @Test
    @DisplayName("사용자 생성 시 프로필 이미지를 넣지 않아도, 정상적으로 사용자 인스턴스가 생성된다.")
    void whenUserCreateByEmptyImageUrlThenUserShouldBeNotNull() {
        final UserJpaEntity userWithoutProfile = new UserJpaEntity(2L, NICKNAME, EMAIL);
        assertAll(
            () -> assertEquals(NICKNAME, userWithoutProfile.getNickname()),
            () -> assertEquals(EMAIL, userWithoutProfile.getEmail()),
            () -> assertNull(userWithoutProfile.getProfileImageUrl()),
            () -> assertFalse(user.isBlacklist())
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
    @DisplayName("닉네임이 빈 문자열일 경우 IllegalArgumentException이 발생한다.")
    void whenNicknameIsEmptyThenIllegalArgumentExceptionShouldBeHappen() {
        assertThatThrownBy(() -> new UserJpaEntity(1L, "", EMAIL))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("올바른 닉네임을 입력해주세요.");
    }

    @ParameterizedTest
    @DisplayName("닉네임이 허용 길이 초과일 경우 IllegalArgumentException이 발생한다.")
    @ValueSource(strings = {"abcdefghijklmnopqrstu", "123456789012345678901", "niㄹffffffckname_too_long"})
    void whenNicknameExceedsMaxLengthThenIllegalArgumentExceptionShouldBeHappen(final String nickname) {
        assertThatThrownBy(() -> new UserJpaEntity(1L, nickname, EMAIL))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("입력 가능한 최대 닉네임 길이를 초과했습니다.");
    }

    @ParameterizedTest
    @DisplayName("닉네임에 허용되지 않는 특수 문자가 포함될 경우 IllegalArgumentException이 발생한다.")
    @ValueSource(strings = {"nick*name", "user@name", "hello!", "name$", "with space"})
    void whenNicknameContainsInvalidCharactersThenIllegalArgumentExceptionShouldBeHappen(final String nickname) {
        assertThatThrownBy(() -> new UserJpaEntity(1L, nickname, EMAIL))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("올바른 닉네임을 입력해주세요.");
    }

    @ParameterizedTest
    @DisplayName("닉네임이 허용 가능한 형식일 경우 정상적으로 인스턴스가 생성된다.")
    @ValueSource(strings = {"validNickname", "nickname123", "NICK_name", "name-with-dash"})
    void whenNicknameIsValidThenEntityShouldBeCreatedSuccessfully(final String nickname) {
        final UserJpaEntity user = new UserJpaEntity(1L, nickname, EMAIL);

        assertNotNull(user);
        assertEquals(nickname, user.getNickname());
    }

    @ParameterizedTest
    @DisplayName("닉네임이 한 글자일 경우에도 정상적으로 인스턴스가 생성된다.")
    @ValueSource(strings = {"acdc", "ef3B", "fas1", "f_ff", "33f-"})
    void whenNicknameIsSingleCharacterThenEntityShouldBeCreatedSuccessfully(final String nickname) {
        final UserJpaEntity user = new UserJpaEntity(1L, nickname, EMAIL);

        assertNotNull(user);
        assertEquals(nickname, user.getNickname());
    }

    @Test
    @DisplayName("닉네임이 최대 허용 길이인 20자일 경우 정상적으로 인스턴스가 생성된다.")
    void whenNicknameIsMaxLengthThenEntityShouldBeCreatedSuccessfully() {
        String maxLengthNickname = "12345678901234567890";
        UserJpaEntity user = new UserJpaEntity(1L, maxLengthNickname, EMAIL);

        assertNotNull(user);
        assertEquals(maxLengthNickname, user.getNickname());
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
    @DisplayName("Role을 스트링으로 변환 시, 정상적으로 성공한다.")
    void whenConvertingRoleToStringThenResultShouldBeCorrectly() {
        final UserJpaEntity newUser = new UserJpaEntity(1L, NICKNAME, EMAIL, NORMAL);

        assertEquals(NORMAL.name(), newUser.getRoleAsString());
    }

    @Test
    @DisplayName("사용자 ID가 같다면, 동일한 객체이다.")
    void whenUserIdSameThenInstanceShouldBeSame() {
        final UserJpaEntity userWithId1 = new UserJpaEntity(1L, NICKNAME, EMAIL);
        final UserJpaEntity anotherUserWithId1 = new UserJpaEntity(1L, NICKNAME, EMAIL);

        assertEquals(userWithId1, anotherUserWithId1);
    }

    @Test
    @DisplayName("객체가 같다면, 동일한 객체이다.")
    void whenUserSameThenInstanceShouldBeSame() {
        final UserJpaEntity userWithId1 = new UserJpaEntity(1L, NICKNAME, EMAIL);
        final UserJpaEntity anotherUserWithId1 = userWithId1;

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
    @DisplayName("Null값과 비교하면, 다른 객체이다.")
    void whenUserIsNullThenInstanceShouldBeDifferent() {
        final UserJpaEntity userWithId1 = new UserJpaEntity(1L, NICKNAME, EMAIL);

        assertNotEquals(userWithId1, null);
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
