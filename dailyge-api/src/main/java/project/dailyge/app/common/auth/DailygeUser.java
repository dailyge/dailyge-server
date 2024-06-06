package project.dailyge.app.common.auth;

import lombok.Getter;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.domain.user.Role;

import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import static project.dailyge.domain.user.Role.ADMIN;
import project.dailyge.domain.user.UserJpaEntity;

import java.util.Objects;

@Getter
public class DailygeUser {

    private static final String USER_NOT_MATCH_MESSAGE = "사용자 정보가 일치 하지 않습니다.";

    private final Long userId;
    private final Role role;

    public DailygeUser(
        final Long userId,
        final Role role
    ) {
        this.userId = userId;
        this.role = role;
    }

    public DailygeUser(final UserJpaEntity user) {
        this.userId = user.getId();
        this.role = user.getRole();
    }

    public boolean isAdmin() {
        return ADMIN.equals(this.role);
    }

    public void isOwner(final Long userId) {
        if (!this.userId.equals(userId)) {
            throw new UnAuthorizedException(USER_NOT_MATCH_MESSAGE, UN_AUTHORIZED);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailygeUser that = (DailygeUser) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
