package project.dailyge.app.test.task.fixture;

import project.dailyge.app.core.task.presentation.requesst.TaskCreateRequest;
import project.dailyge.app.core.task.presentation.requesst.TaskStatusUpdateRequest;
import project.dailyge.app.core.task.presentation.requesst.TaskUpdateRequest;
import project.dailyge.entity.task.TaskColor;
import project.dailyge.entity.task.TaskStatus;
import static project.dailyge.entity.task.TaskStatus.DONE;
import static project.dailyge.entity.task.TaskStatus.IN_PROGRESS;

import java.time.LocalDate;

public final class TaskRequestFixture {

    private TaskRequestFixture() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static TaskCreateRequest createTaskRegisterRequest(
        final Long monthlyTaskId,
        final LocalDate now
    ) {
        return new TaskCreateRequest(
            monthlyTaskId,
            "주간 미팅",
            "Backend 팀과 Api 스펙 정의",
            TaskColor.BLUE,
            now
        );
    }

    public static TaskUpdateRequest createTaskUpdateRequest(
        final Long monthlyTaskId,
        final LocalDate now
    ) {
        return new TaskUpdateRequest(
            monthlyTaskId,
            "Api 스펙 수정",
            "Backend 팀과 Api 스펙 수정 회의",
            now,
            IN_PROGRESS,
            TaskColor.BLUE
        );
    }

    public static TaskStatusUpdateRequest createTaskStatusUpdateRequest(final Long monthlyTaskId) {
        return new TaskStatusUpdateRequest(
            monthlyTaskId,
            DONE
        );
    }

    public static TaskStatusUpdateRequest createTaskStatusUpdateRequest(
        final Long monthlyTaskId,
        final TaskStatus status
    ) {
        return new TaskStatusUpdateRequest(
            monthlyTaskId,
            status
        );
    }
}
