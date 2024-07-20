package project.dailyge.app.common.auth;

import lombok.Getter;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import project.dailyge.app.common.exception.UnAuthorizedException;
import static project.dailyge.app.common.exception.UnAuthorizedException.USER_NOT_MATCH_MESSAGE;
import project.dailyge.entity.user.Role;
import static project.dailyge.entity.user.Role.ADMIN;
import project.dailyge.entity.user.UserJpaEntity;

import java.util.Objects;

@Getter
public class DailygeUser {

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

    public Long getId() {
        return userId;
    }

    public boolean isAdmin() {
        return ADMIN.equals(this.role);
    }

    public void validateAuth(final Long userId) {
        if (!this.userId.equals(userId)) {
            throw new UnAuthorizedException(USER_NOT_MATCH_MESSAGE, UN_AUTHORIZED);
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final DailygeUser that = (DailygeUser) obj;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
