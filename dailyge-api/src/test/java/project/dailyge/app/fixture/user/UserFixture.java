package project.dailyge.app.fixture.user;

import project.dailyge.domain.user.UserJpaEntity;

import java.time.LocalDateTime;

public final class UserFixture {

    private UserFixture() {
        throw new AssertionError("올바르지 않는 생성 방식입니다.");
    }

    public static final String NICKNAME = "test";
    public static final String EMAIL = "test@email.com";

    public static UserJpaEntity createUserJpaEntity(Long userId) {
        return new UserJpaEntity(userId, NICKNAME, EMAIL);
    }

    public static UserJpaEntity createUserJpaEntity() {
        return new UserJpaEntity(null, NICKNAME, EMAIL, LocalDateTime.now());
    }
}
