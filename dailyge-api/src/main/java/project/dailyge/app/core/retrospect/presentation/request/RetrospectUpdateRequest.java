package project.dailyge.app.core.retrospect.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.hibernate.validator.constraints.Length;
import project.dailyge.app.core.retrospect.application.command.RetrospectUpdateCommand;

public record RetrospectUpdateRequest(
    @Length(min = 1, max = 50)
    @NotNull(message = "회고 제목을 입력해 주세요.")
    @NotBlank(message = "회고 제목은 공백일 수 없습니다.")
    String title,

    @Length(min = 1, max = 3000)
    @NotNull(message = "회고 내용을 입력해 주세요.")
    @NotBlank(message = "회고 내용은 공백일 수 없습니다.")
    String content,

    @NotNull(message = "회고 날짜를 입력해 주세요.")
    LocalDate date,

    @NotNull(message = "공개여부를 입력해 주세요.")
    boolean isPublic
) {

    public RetrospectUpdateCommand toCommand() {
        return new RetrospectUpdateCommand(title, content, date.atTime(0, 0, 0, 0), isPublic);
    }

    @Override
    public String toString() {
        return String.format(
            "{\"title\":\"%s\",\"content\":\"%s\",\"date\":\"%s\",\"isPublic\":\"%s\"}",
            title, content, date, isPublic
        );
    }
}
