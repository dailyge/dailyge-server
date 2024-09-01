package project.dailyge.app.common.auth;

import java.util.Objects;
import lombok.Getter;
import project.dailyge.entity.user.Role;
import static project.dailyge.entity.user.Role.ADMIN;

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

    public boolean isAdmin() {
        return ADMIN.equals(this.role);
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
