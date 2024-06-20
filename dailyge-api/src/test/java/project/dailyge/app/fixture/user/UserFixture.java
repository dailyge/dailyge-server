package project.dailyge.app.fixture.user;

import project.dailyge.entity.user.UserJpaEntity;

public final class UserFixture {

    private UserFixture() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static final String NICKNAME = "test";
    public static final String EMAIL = "test@gmail.com";

    public static UserJpaEntity createUserJpaEntity(Long userId) {
        return new UserJpaEntity(userId, NICKNAME, EMAIL);
    }

    public static UserJpaEntity createUserJpaEntity() {
        return new UserJpaEntity(NICKNAME, EMAIL);
    }
}
