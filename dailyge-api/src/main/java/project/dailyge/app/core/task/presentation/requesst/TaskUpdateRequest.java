package project.dailyge.app.core.task.presentation.requesst;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import project.dailyge.entity.task.TaskStatus;

import java.time.LocalDate;

public record TaskUpdateRequest(
    @Length(min = 1, max = 150)
    @NotNull(message = "제목을 입력해주세요.")
    @NotBlank(message = "제목은 공백일 수 없습니다.")
    String title,

    @Length(min = 1, max = 2500)
    @NotNull(message = "내용을 입력해주세요.")
    @NotBlank(message = "내용은 공백일 수 없습니다.")
    String content,

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "날짜를 입력해주세요.")
    LocalDate date,

    @NotNull(message = "할 일 상태를 입력해주세요.")
    TaskStatus status
) {
}
