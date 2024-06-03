package project.dailyge.app.core.task.application;

import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.domain.task.TaskJpaEntity;

public interface TaskReadUseCase {
    TaskJpaEntity findById(DailygeUser dailygeUser, Long taskId);
}
