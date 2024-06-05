package project.dailyge.app.core.task.application;

import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.domain.task.TaskJpaEntity;

public interface TaskWriteUseCase {
    TaskJpaEntity save(TaskJpaEntity task);

    void delete(DailygeUser dailygeUser, Long taskId);
}
