package project.dailyge.app.core.anniversary.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import project.dailyge.app.core.anniversary.application.command.AnniversaryUpdateCommand;

import java.time.LocalDate;

@Getter
public class AnniversaryUpdateRequest {

    @NotBlank
    @Size(min = 1, max = 50, message = "")
    private String name;

    @NotNull
    private LocalDate date;

    private boolean remind;
    private Long emojiId;

    private AnniversaryUpdateRequest() {
    }

    public AnniversaryUpdateRequest(
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

    public AnniversaryUpdateCommand toCommand() {
        return new AnniversaryUpdateCommand(name, date.atTime(0, 0, 0, 0), remind, emojiId);
    }

    @Override
    public String toString() {
        return String.format(
            "{\"name\":\"%s\", \"date\":\"%s\", \"remind\":%b, \"emojiId\":%d}",
            name, date.toString(), remind, emojiId != null ? emojiId : null
        );
    }
}
