package project.dailyge.app.test.task.fixture;

import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.command.TaskRecurrenceCreateCommand;

import java.time.LocalDate;
import java.util.List;

import static project.dailyge.entity.task.RecurrenceType.WEEKLY;
import static project.dailyge.entity.task.TaskColor.GRAY;

public final class TaskRecurrenceCommandFixture {

    private TaskRecurrenceCommandFixture() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요");
    }

    public static TaskRecurrenceCreateCommand createTaskRecurrenceCreateCommand(
        final LocalDate startDate,
        final LocalDate endDate,
        final DailygeUser dailygeUser
    ) {
        return new TaskRecurrenceCreateCommand(
            "수영",
            "오전 7시 반",
            GRAY,
            WEEKLY,
            List.of(1, 3, 5),
            startDate,
            endDate,
            dailygeUser.getUserId()
        );
    }
}
