package project.dailyge.app.test.user.fixture;

import static project.dailyge.entity.user.Role.ADMIN;
import project.dailyge.entity.user.UserJpaEntity;

public final class UserFixture {

    private UserFixture() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static final String NICKNAME = "dailyge";
    public static final String EMAIL = "dailyge@gmail.com";

    public static UserJpaEntity createUser(final Long userId) {
        return new UserJpaEntity(userId, NICKNAME, EMAIL);
    }

    public static UserJpaEntity createUser() {
        return new UserJpaEntity(NICKNAME, EMAIL);
    }

    public static UserJpaEntity createUser(
        final String nickname,
        final String email
    ) {
        return new UserJpaEntity(nickname, email);
    }

    public static UserJpaEntity createAdminUser() {
        return new UserJpaEntity(NICKNAME, EMAIL, ADMIN);
    }

    public static UserJpaEntity createAdminUser(
        final String nickname,
        final String email
    ) {
        return new UserJpaEntity(nickname, email, ADMIN);
    }
}
