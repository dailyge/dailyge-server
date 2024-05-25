package project.dailyge.app.core.task.persistence;

import project.dailyge.domain.task.*;

public interface TaskEntityWriteRepository {
    Task save(Task task);
}
