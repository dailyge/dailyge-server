package project.dailyge.app.core.user.presentation.request;

import jakarta.validation.constraints.NotNull;

public record LogoutRequest(
    @NotNull(message = "사용자 ID를 입력해주세요.")
    Long userId
) {
}
