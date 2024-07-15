package project.dailyge.app.core.task.presentation.requesst;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MonthlyTasksRegisterRequest(
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate date
) {
}
