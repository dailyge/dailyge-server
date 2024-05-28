package project.dailyge.app.fixture.user;

import project.dailyge.domain.user.UserJpaEntity;

public final class UserFixture {

    private UserFixture() {}

    public static final String NICKNAME = "test";
    public static final String EMAIL = "test@email.com";

    public static UserJpaEntity createUserJpaEntity(Long userId) {
        return new UserJpaEntity(userId, NICKNAME, EMAIL);
    }
}
