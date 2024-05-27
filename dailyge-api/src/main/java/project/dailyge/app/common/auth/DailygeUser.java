package project.dailyge.app.common.auth;

import lombok.Getter;
import project.dailyge.domain.user.User;

import java.util.Objects;

@Getter
public class DailygeUser {

    private final Long userId;

    public DailygeUser(final Long userId) {
        this.userId = userId;
    }

    public DailygeUser(final User user) {
        this.userId = user.getId();
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
