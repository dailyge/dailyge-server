package project.dailyge.app.core.task.application;

import project.dailyge.domain.task.*;

public interface TaskWriteUseCase {
    Task save(Task task);
}
