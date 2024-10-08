package project.dailyge.app.core.user.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import project.dailyge.app.core.user.application.command.UserBlacklistCreateCommand;

public record UserBlacklistCreateRequest(
    @NotNull
    @NotBlank
    String accessToken
) {
    public UserBlacklistCreateCommand toCommand() {
        return new UserBlacklistCreateCommand(accessToken);
    }

    @Override
    public String toString() {
        return String.format("{\"accessToken\":\"%s\"}", accessToken);
    }
}
