package project.dailyge.app.common.auth;

import lombok.*;
import project.dailyge.domain.user.*;

import java.util.*;

@Getter
public class DailygeUser {

    private final Long userId;

    public DailygeUser(Long userId) {
        this.userId = userId;
    }

    public DailygeUser(User user) {
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
