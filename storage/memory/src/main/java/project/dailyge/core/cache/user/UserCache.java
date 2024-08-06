package project.dailyge.core.cache.user;

import lombok.Getter;

import java.util.Objects;

@Getter
public class UserCache {

    private Long id;
    private String nickname;
    private String email;
    private String profileImageUrl;
    private String role;

    private UserCache() {
    }

    public UserCache(
        final Long id,
        final String nickname,
        final String email,
        final String profileImageUrl,
        final String role
    ) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final UserCache that = (UserCache) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
