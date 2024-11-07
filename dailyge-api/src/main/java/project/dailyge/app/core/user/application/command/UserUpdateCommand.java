package project.dailyge.app.core.user.application.command;

public record UserUpdateCommand(String nickname) {
    @Override
    public String toString() {
        return String.format("{\"nickname\":\"%s\"}", nickname);
    }
}
