package project.dailyge.app.core.user.presentation.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import project.dailyge.domain.user.UserJpaEntity;

public record UserRegisterRequest(
    @Length(min = 1, max = 20)
    @NotBlank(message = "닉네임을 입력해주세요.")
    String nickname,

    @Length(min = 1, max = 50)
    @NotBlank(message = "제목을 입력해주세요.")
    String email,

    @Length(min = 1, max = 150)
    String profileImageUrl
) {

    public UserJpaEntity toEntity() {
        if (profileImageUrl != null) {
            return new UserJpaEntity(nickname, email, profileImageUrl);
        }
        return new UserJpaEntity(nickname, email);
    }

    public String toString() {
        return String.format("{nickname: %s, email: %s, profileImageUrl: %s}",
            nickname, email, profileImageUrl);
    }
}
