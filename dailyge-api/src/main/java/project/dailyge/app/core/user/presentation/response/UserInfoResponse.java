package project.dailyge.app.core.user.presentation.response;

import project.dailyge.core.cache.user.UserCache;

public class UserInfoResponse {

    private final Long userId;
    private final String nickname;
    private final String email;
    private final String profileImageUrl;

    public UserInfoResponse(final UserCache user) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.profileImageUrl = user.getProfileImageUrl();
    }

    public Long getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    @Override
    public String toString() {
        return String.format(
            "userId: %s, nickname: %s, email: %s, profileImageUrl: %s",
            userId, nickname, email, profileImageUrl
        );
    }
}
