package project.dailyge.app.common.auth;

import lombok.Getter;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;

import project.dailyge.app.common.exception.CommonException;
import project.dailyge.entity.user.Role;

import static project.dailyge.app.core.user.exception.UserCodeAndMessage.*;
import static project.dailyge.entity.user.Role.ADMIN;

import java.util.Objects;

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
        if (isAdmin()) {
            return;
        }
        if (!this.userId.equals(userId)) {
            throw CommonException.from(USER_NOT_MATCH.message(), UN_AUTHORIZED);
        }
    }

    public boolean isValid(final Long userId) {
        return this.userId.equals(userId);
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
