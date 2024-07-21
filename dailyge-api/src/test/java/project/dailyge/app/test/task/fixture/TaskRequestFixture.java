package project.dailyge.app.test.task.fixture;

import project.dailyge.app.core.task.presentation.requesst.TaskUpdateRequest;
import static project.dailyge.entity.task.TaskStatus.IN_PROGRESS;

import java.time.LocalDate;

public final class TaskRequestFixture {

    private TaskRequestFixture() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static TaskUpdateRequest createUpdateRequest(
        final String documentId,
        final LocalDate now
    ) {
        return new TaskUpdateRequest(
            documentId,
            "Api 스펙 수정",
            "Backend 팀과 Api 스펙 수정 회의",
            now,
            IN_PROGRESS
        );
    }
}
