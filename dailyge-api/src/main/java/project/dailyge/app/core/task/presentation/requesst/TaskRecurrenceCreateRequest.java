package project.dailyge.app.core.task.presentation.requesst;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import project.dailyge.app.core.task.application.command.TaskRecurrenceCreateCommand;
import project.dailyge.entity.task.RecurrenceType;
import project.dailyge.entity.task.TaskColor;

import java.time.LocalDate;
import java.util.List;

public class TaskRecurrenceCreateRequest {

    @Length(min = 1, max = 150)
    @NotNull(message = "제목을 입력해 주세요.")
    @NotBlank(message = "제목은 공백일 수 없습니다.")
    private String title;

    @Length(min = 1, max = 2500)
    @NotNull(message = "내용을 입력해 주세요.")
    @NotBlank(message = "내용은 공백일 수 없습니다.")
    private String content;

    @NotNull(message = "색상을 입력해주세요.")
    private TaskColor color;

    @NotNull(message = "반복 유형을 선택해주세요.")
    private RecurrenceType type;

    private List<Integer> datePattern;
    private LocalDate startDate;
    private LocalDate endDate;

    private TaskRecurrenceCreateRequest() {
    }

    public TaskRecurrenceCreateRequest(
        final String title,
        final String content,
        final TaskColor color,
        final RecurrenceType type,
        final List<Integer> dayPattern,
        final LocalDate startDate,
        final LocalDate endDate
    ) {
        this.title = title;
        this.content = content;
        this.color = color;
        this.type = type;
        this.datePattern = dayPattern;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public TaskRecurrenceCreateCommand toCommand() {
        return new TaskRecurrenceCreateCommand(title, content, color, type, datePattern, startDate, endDate);
    }
}
