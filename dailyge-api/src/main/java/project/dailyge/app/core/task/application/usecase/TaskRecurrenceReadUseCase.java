package project.dailyge.app.core.task.application.usecase;

import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskRecurrenceReadService;
import project.dailyge.app.core.task.persistence.TaskRecurrenceReadDao;
import project.dailyge.app.paging.Cursor;
import project.dailyge.entity.task.TaskRecurrenceJpaEntity;

import java.util.List;

@ApplicationLayer(value = "TaskRecurrenceReadUseCase")
public class TaskRecurrenceReadUseCase implements TaskRecurrenceReadService {

    private final TaskRecurrenceReadDao taskRecurrenceReadDao;

    public TaskRecurrenceReadUseCase(final TaskRecurrenceReadDao taskRecurrenceReadDao) {
        this.taskRecurrenceReadDao = taskRecurrenceReadDao;
    }

    @Override
    public List<TaskRecurrenceJpaEntity> findTaskRecurrencesByCursor(
        final DailygeUser dailygeUser,
        final Cursor cursor
    ) {
        return taskRecurrenceReadDao.findByCursor(dailygeUser.getUserId(), cursor);
    }
}
