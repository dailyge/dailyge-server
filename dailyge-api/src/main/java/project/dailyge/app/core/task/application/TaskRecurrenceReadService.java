package project.dailyge.app.core.task.application;

import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.paging.Cursor;
import project.dailyge.entity.task.TaskRecurrenceJpaEntity;

import java.util.List;

public interface TaskRecurrenceReadService {
    List<TaskRecurrenceJpaEntity> findTaskRecurrencesByCursor(DailygeUser dailygeUser, Cursor cursor);
}
