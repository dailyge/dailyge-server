package project.dailyge.app.test.user.fixture;

import project.dailyge.entity.user.UserJpaEntity;

public final class UserFixture {

    private UserFixture() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static final String NICKNAME = "test";
    public static final String EMAIL = "test@gmail.com";

    public static UserJpaEntity createUser(final Long userId) {
        return new UserJpaEntity(userId, NICKNAME, EMAIL);
    }

    public static UserJpaEntity createUser(
        final Long userId,
        final String nickname,
        final String email
    ) {
        return new UserJpaEntity(userId, nickname, email);
    }

}
