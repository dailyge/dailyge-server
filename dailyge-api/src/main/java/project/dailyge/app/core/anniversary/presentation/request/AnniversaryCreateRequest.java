package project.dailyge.app.core.anniversary.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import project.dailyge.app.core.anniversary.application.command.AnniversaryCreateCommand;

import java.time.LocalDate;

@Getter
public class AnniversaryCreateRequest {

    @NotBlank
    private String name;

    @NotNull
    private LocalDate date;

    private boolean remind;
    private Long emojiId;

    private AnniversaryCreateRequest() {
    }

    public AnniversaryCreateRequest(
        final String name,
        final LocalDate date,
        final boolean remind,
        final Long emojiId
    ) {
        this.name = name;
        this.date = date;
        this.remind = remind;
        this.emojiId = emojiId;
    }

    public AnniversaryCreateCommand toCommand() {
        return new AnniversaryCreateCommand(name, date.atTime(0, 0), remind, emojiId);
    }

    @Override
    public String toString() {
        return String.format(
            "{\"name\":\"%s\", \"date\":\"%s\", \"remind\":%b, \"emojiId\":%d}",
            name, date.toString(), remind, emojiId != null ? emojiId : null
        );
    }
}
