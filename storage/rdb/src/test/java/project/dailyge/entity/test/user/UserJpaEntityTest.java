package project.dailyge.entity.test.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import project.dailyge.entity.user.UserJpaEntity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static project.dailyge.entity.user.UserJpaEntity.*;

@DisplayName("[UnitTest] 유저 엔티티 테스트")
class UserJpaEntityTest {

	private static final String NICKNAME = "nickname";
	private static final String EMAIL = "email@gmail.com";
	private static final String PROFILE_IMAGE_URL = "profileImgUrl.jpg";

	@Test
	@DisplayName("사용자 생성 시, 정상적으로 사용자 인스턴스가 생성된다.")
	void whenUserCreateThenUserShouldBeNotNull() {
		final UserJpaEntity user = new UserJpaEntity(NICKNAME, EMAIL, PROFILE_IMAGE_URL);

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
		final UserJpaEntity user = new UserJpaEntity(NICKNAME, EMAIL);

		assertAll(
			() -> assertEquals(NICKNAME, user.getNickname()),
			() -> assertEquals(EMAIL, user.getEmail()),
			() -> assertNull(user.getProfileImageUrl())
		);
	}

	@Test
	@DisplayName("허용 가능한 닉네임 길이를 초과하면, IllegalArgumentException이 발생한다.")
	void whenMaxLengthOverNicknameThenIllegalArgumentExceptionShouldBeHappen() {
		final String overLengthNickname = "n".repeat(21);

		assertThatThrownBy(() -> new UserJpaEntity(overLengthNickname, EMAIL))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage(getOverMaxNicknameLengthErrorMessage());
	}

	@Test
	@DisplayName("허용 가능한 이메일 길이를 초과하면 IllegalArgumentException이 발생한다.")
	void whenMaxLengthOverEmailThenIllegalArgumentExceptionShouldBeHappen() {
		final String overLengthEmail = "e".repeat(50) + "@domain.com";

		assertThatThrownBy(() -> new UserJpaEntity(NICKNAME, overLengthEmail))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage(getOverMaxEmailLengthErrorMessage());
	}

	@ParameterizedTest
	@DisplayName("이메일 규격에 맞지 않을 경우 IllegalArgumentException이 발생한다.")
	@ValueSource(strings = {"email.domain.com", "email@damain,com", "email@domain@domain.com"})
	void whenNotCompliantEmailThenIllegalArgumentExceptionShouldBeHappen(String notCompliantEmail){
		assertThatThrownBy(() -> new UserJpaEntity(NICKNAME, notCompliantEmail))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage(UserJpaEntity.getInvalidEmailErrorMessage());
	}

	@Test
	@DisplayName("허용 가능한 프로필 이미지 URL 길이를 초과하면 IllegalArgumentException이 발생한다.")
	void whenMaxLengthOverProfileImageUrlThenIllegalArgumentExceptionShouldBeHappen() {
		final String overLengthProfileImageUrl = "p".repeat(2001);

		assertThatThrownBy(() -> new UserJpaEntity(NICKNAME, EMAIL, overLengthProfileImageUrl))
			.isExactlyInstanceOf(IllegalArgumentException.class)
			.hasMessage(getOverMaxProfileImageUrlErrorMessage());
	}

	@Test
	@DisplayName("사용자 ID가 같다면, 동일한 객체이다.")
	void whenUserIdSameThenInstanceShouldBeSame() {
		UserJpaEntity user1 = new UserJpaEntity(1L, "test", "test@gmail.com");
		UserJpaEntity user2 = new UserJpaEntity(1L, "test", "test@gmail.com");

		assertEquals(user1, user2);
	}

	@Test
	@DisplayName("사용자 ID가 다르다면, 다른 객체이다.")
	void whenUserIdDifferentThenInstanceShouldBeDifferent() {
		UserJpaEntity user1 = new UserJpaEntity(1L, "test", "test@gmail.com");
		UserJpaEntity user2 = new UserJpaEntity(2L, "test", "test@gmail.com");

		assertNotEquals(user1, user2);
	}

	@Test
	@DisplayName("사용자 ID가 같다면, 해시코드가 동일하다.")
	void whenUserIdSameThenHashcodeShouldBeSame() {
		UserJpaEntity user1 = new UserJpaEntity(1L, "test", "test@gmail.com");
		UserJpaEntity user2 = new UserJpaEntity(1L, "test", "test@gmail.com");

		assertEquals(user1.hashCode(), user2.hashCode());
	}

	@Test
	@DisplayName("사용자 ID가 다르다면, 해시코드는 다른 값이다.")
	void whenUserIdDifferentThenHashcodeShouldBeDifferent() {
		UserJpaEntity user1 = new UserJpaEntity(1L, "test", "test@gmail.com");
		UserJpaEntity user2 = new UserJpaEntity(2L, "test", "test@gmail.com");

		assertNotEquals(user1.hashCode(), user2.hashCode());
	}
}
