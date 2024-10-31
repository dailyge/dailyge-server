package project.dailyge.app.core.common.auth;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import project.dailyge.app.common.exception.CommonException;
import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_NOT_MATCH;
import project.dailyge.entity.user.Role;
import static project.dailyge.entity.user.Role.ADMIN;

import java.util.Objects;

public class DailygeUser {

    private Long userId;
    private Role role;
    private Long countryId;

    private DailygeUser() {
    }

    public DailygeUser(
        final Long userId,
        final Role role,
        final Long countryId
    ) {
        this.userId = userId;
        this.role = role;
        this.countryId = countryId;
    }

    public DailygeUser(
        final Long userId,
        final Role role
    ) {
        this(userId, role, 1L);
    }

    public Long getId() {
        return userId;
    }

    public String getIdAsString() {
        return userId.toString();
    }

    public Long getUserId() {
        return userId;
    }

    public Role getRole() {
        return role;
    }

    public Long getCountryId() {
        return countryId;
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
        if (isAdmin()) {
            return true;
        }
        return this.userId.equals(userId);
    }

    public static DailygeUser getDailygeUser(final Object obj) {
        try {
            if (obj != null) {
                return (DailygeUser) obj;
            }
        } catch (ClassCastException ex) {
            return null;
        }
        return null;
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
