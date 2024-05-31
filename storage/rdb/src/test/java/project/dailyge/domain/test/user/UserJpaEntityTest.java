package project.dailyge.domain.test.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import project.dailyge.domain.user.UserJpaEntity;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[UnitTest] 유저 엔티티 테스트")
class UserJpaEntityTest {

	private static final String NICKNAME = "nickname";
	private static final String EMAIL = "email@domain.com";
	private static final String PROFILE_IMAGE_URL = "profileImgUrl.jpg";

	@Test
	@DisplayName("사용자 생성하는데 성공한다.")
	void userCreateSuccessTest() {
		final UserJpaEntity user = new UserJpaEntity(NICKNAME, EMAIL);

		assertAll(
			() -> assertEquals(NICKNAME, user.getNickname()),
			() -> assertEquals(EMAIL, user.getEmail())
		);
	}

	@Test
	@DisplayName("프로필 이미지를 포함하여 생성하는데 성공한다.")
	void userProfileImageUrlCreateSuccessTest() {
		final UserJpaEntity user = new UserJpaEntity(NICKNAME, EMAIL, PROFILE_IMAGE_URL);

		assertAll(
			() -> assertEquals(NICKNAME, user.getNickname()),
			() -> assertEquals(EMAIL, user.getEmail()),
			() -> assertEquals(PROFILE_IMAGE_URL, user.getProfileImageUrl())
		);
	}

	@Test
	@DisplayName("허용 가능한 닉네임 길이를 초과하면 IllegalArgumentException이 발생한다.")
	void whenMaxLengthOverNicknameThenIllegalArgumentException() {
		final String overLengthNickname = "n".repeat(21);

		assertThrowsExactly(IllegalArgumentException.class, () -> new UserJpaEntity(overLengthNickname, EMAIL));
	}

	@Test
	@DisplayName("허용 가능한 이메일 길이를 초과하면 IllegalArgumentException이 발생한다.")
	void whenMaxLengthOverEmailThenIllegalArgumentException() {
		final String overLengthEmail = "e".repeat(50) + "@domain.com";

		assertThrowsExactly(IllegalArgumentException.class, () -> new UserJpaEntity(NICKNAME, overLengthEmail));
	}

	@ParameterizedTest
	@DisplayName("이메일 규격에 맞지 않을 경우 IllegalArgumentException이 발생한다.")
	@ValueSource(strings = {"email.domain.com", "email@damain,com", "email@domain@domain.com"})
	void whenNotCompliantEmailThenIllegalArgumentException(String notCompliantEmail){
		assertThrowsExactly(IllegalArgumentException.class, () -> new UserJpaEntity(NICKNAME, notCompliantEmail));
	}

	@Test
	@DisplayName("허용 가능한 프로필 이미지 URL 길이를 초과하면 IllegalArgumentException이 발생한다.")
	void whenMaxLengthOverProfileImageUrlThenIllegalArgumentException() {
		final String overLengthProfileImageUrl = "p".repeat(2001);

		assertThrowsExactly(IllegalArgumentException.class, () -> new UserJpaEntity(NICKNAME, EMAIL, overLengthProfileImageUrl));
	}

}
