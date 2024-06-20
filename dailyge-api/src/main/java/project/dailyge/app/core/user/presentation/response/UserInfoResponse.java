package project.dailyge.app.core.user.presentation.response;

import lombok.Getter;
import project.dailyge.entity.user.UserJpaEntity;

@Getter
public class UserInfoResponse {

    private final Long userId;
    private final String nickname;
    private final String email;
    private final String profileImageUrl;

    public UserInfoResponse(final UserJpaEntity user) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.profileImageUrl = user.getProfileImageUrl();
    }

    @Override
    public String toString() {
        return String.format(
            "userId: %s, nickname: %s, email: %s, profileImageUrl: %s",
            userId, nickname, email, profileImageUrl
        );
    }
}
