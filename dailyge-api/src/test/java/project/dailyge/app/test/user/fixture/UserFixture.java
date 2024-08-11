package project.dailyge.app.test.user.fixture;

import static project.dailyge.entity.user.Role.ADMIN;
import static project.dailyge.entity.user.Role.NORMAL;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.entity.user.UserJpaEntity;

public final class UserFixture {

    private UserFixture() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static final long ID = 1L;
    public static final String NICKNAME = "test";
    public static final String EMAIL = "test@gmail.com";
    public static final String PROFILE_IMAGE_URL = "https://shorturl.at/VbXmh";
    public static final UserJpaEntity user = new UserJpaEntity(
        ID,
        NICKNAME,
        EMAIL,
        PROFILE_IMAGE_URL
    );
    public static final UserCache userCache = new UserCache(
        ID,
        NICKNAME,
        EMAIL,
        PROFILE_IMAGE_URL,
        NORMAL.name()
    );

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

    public static UserJpaEntity createAdminUser(
        final Long id,
        final String nickname,
        final String email
    ) {
        return new UserJpaEntity(id, nickname, email, ADMIN);
    }
}
