package project.dailyge.core.cache.user;

import lombok.Getter;

@Getter
public class UserCache {

    private Long id;
    private String nickname;

    private UserCache() {
    }

    public UserCache(
        final Long id,
        final String nickname
    ) {
        this.id = id;
        this.nickname = nickname;
    }
}
