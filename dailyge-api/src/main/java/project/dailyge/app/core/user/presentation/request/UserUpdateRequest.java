package project.dailyge.app.core.user.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import project.dailyge.app.core.user.application.command.UserUpdateCommand;

public record UserUpdateRequest(
    @Length(min = 1, max = 20)
    @NotNull(message = "닉네임을 입력해 주세요.")
    @NotBlank(message = "닉네임은 공백일 수 없습니다.")
    String nickname
) {

    public UserUpdateCommand toCommand() {
        return new UserUpdateCommand(nickname);
    }

    @Override
    public String toString() {
        return String.format("{\"nickname\":\"%s\"}", nickname);
    }
}
