package project.dailyge.app.core.task.presentation.requesst;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MonthlyTasksCreateRequest(
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate date
) {
    @Override
    public String toString() {
        return String.format("{\"date\":\"%s\"}", date);
    }
}
