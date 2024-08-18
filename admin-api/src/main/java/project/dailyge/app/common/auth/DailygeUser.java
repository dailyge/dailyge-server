package project.dailyge.app.common.auth;

import java.util.Objects;
import lombok.Getter;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.entity.user.Role;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import static project.dailyge.app.common.exception.UnAuthorizedException.USER_NOT_MATCH_MESSAGE;
import static project.dailyge.entity.user.Role.ADMIN;

@Getter
public class DailygeUser {

    private Long userId;
    private Role role;

    private DailygeUser() {
    }

    public DailygeUser(
        final Long userId,
        final Role role
    ) {
        this.userId = userId;
        this.role = role;
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

    @Override
    public String toString() {
        return String.format("{\"userId\":\"%s\",\"role\":\"%s\"}", userId, role);
    }
}
