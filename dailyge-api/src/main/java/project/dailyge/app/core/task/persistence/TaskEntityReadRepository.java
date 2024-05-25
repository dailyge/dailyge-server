package project.dailyge.app.core.task.persistence;

import project.dailyge.domain.user.*;

public interface TaskEntityReadRepository {
    User findById(Long userId);
}
