package project.dailyge.app.test.task.fixture;

import static java.time.LocalDate.now;
import project.dailyge.app.core.task.application.command.TaskCreateCommand;
import project.dailyge.app.core.task.application.command.TaskUpdateCommand;
import static project.dailyge.entity.task.TaskStatus.IN_PROGRESS;

public final class TaskCommandFixture {

    private TaskCommandFixture() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요");
    }

    public static TaskCreateCommand createTaskCreationCommand(final String monthlyTaskId) {
        return new TaskCreateCommand(
            monthlyTaskId,
            "Backend 팀과 Api 스펙 정의 미팅.",
            "오전 9시 A 회의실에서 Backend 팀과 Api 스펙 정의 미팅.",
            now()
        );
    }

    public static TaskUpdateCommand createTaskUpdateCommand(final String monthlyTaskId) {
        return new TaskUpdateCommand(
            monthlyTaskId,
            "Backend 팀과 Api 스펙 정의 미팅.",
            "오전 9시 A 회의실에서 Backend 팀과 Api 스펙 정의 미팅.",
            now(),
            IN_PROGRESS
        );
    }
}
