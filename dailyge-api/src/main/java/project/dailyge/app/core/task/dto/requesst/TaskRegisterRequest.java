package project.dailyge.app.core.task.dto.requesst;

import project.dailyge.app.common.auth.*;
import project.dailyge.domain.task.*;

import java.time.*;

public record TaskRegisterRequest(
    String title,
    String content,
    LocalDate date
) {

    public Task toEntity(DailygeUser user) {
        return new Task(
            title,
            content,
            date,
            TaskStatus.TODO,
            user.getUserId()
        );
    }
}
