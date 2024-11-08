package project.dailyge.core.cache.user;

import java.io.Serializable;
import java.util.Objects;

public class UserCache implements Serializable {

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

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImageUrl() {
        if (profileImageUrl == null) {
            return "";
        }
        return profileImageUrl;
    }

    public String getRole() {
        return role;
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

    @Override
    public String toString() {
        return String.format(
            "{\"id\":%d, \"nickname\":\"%s\", \"email\":\"%s\", \"profileImageUrl\":\"%s\", \"role\":\"%s\"}",
            id, nickname, email, profileImageUrl, role
        );
    }
}
