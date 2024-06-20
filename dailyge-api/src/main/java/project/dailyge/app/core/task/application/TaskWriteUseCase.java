package project.dailyge.app.core.task.application;

import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.command.TaskUpdateCommand;
import project.dailyge.entity.task.TaskJpaEntity;

public interface TaskWriteUseCase {
    TaskJpaEntity save(TaskJpaEntity task);

    void update(DailygeUser dailygeUser, Long taskId, TaskUpdateCommand command);

    void delete(DailygeUser dailygeUser, Long taskId);
}
