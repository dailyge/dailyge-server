package project.dailyge.core.cache.user;

import lombok.Getter;

@Getter
public class UserCache {

    private Long id;
    private String nickname;
    private String email;
    private String profileImageUrl;

    private UserCache() {
    }

    public UserCache(
        final Long id,
        final String nickname,
        final String email,
        final String profileImageUrl
    ) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }
}
