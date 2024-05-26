package project.dailyge.app.core.task.dto.requesst;

import jakarta.validation.constraints.*;
import project.dailyge.app.common.auth.*;
import project.dailyge.domain.task.*;

import java.time.*;

public record TaskRegisterRequest(
    @NotNull(message = "제목을 입력해주세요.")
    @NotBlank(message = "제목은 공백일 수 없습니다.")
    String title,

    @NotNull(message = "내용을 입력해주세요.")
    @NotBlank(message = "내용은 공백일 수 없습니다.")
    String content,

    @NotNull(message = "날짜를 입력해주세요.")
    LocalDate date
) {

    public TaskJpaEntity toEntity(DailygeUser user) {
        return new TaskJpaEntity(
            title,
            content,
            date,
            TaskStatus.TODO,
            user.getUserId()
        );
    }

    @Override
    public String toString() {
        return String.format("{title: %s, content: %s, date: %s}", title, content, date);
    }
}
