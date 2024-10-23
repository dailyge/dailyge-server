package project.dailyge.app.core.task.application.usecase;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskReadService;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.MONTHLY_TASK_NOT_FOUND;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_NOT_FOUND;
import project.dailyge.app.core.task.exception.TaskTypeException;
import project.dailyge.entity.task.MonthlyTaskEntityReadRepository;
import project.dailyge.entity.task.MonthlyTaskJpaEntity;
import project.dailyge.entity.task.TaskEntityReadRepository;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.task.Tasks;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@ApplicationLayer(value = "TaskReadUseCase")
class TaskReadUseCase implements TaskReadService {

    private final TaskEntityReadRepository taskReadRepository;
    private final MonthlyTaskEntityReadRepository monthlyTaskReadRepository;

    @Override
    public Long findMonthlyTaskId(
        final DailygeUser dailygeUser,
        final LocalDate date
    ) {
        return monthlyTaskReadRepository.findMonthlyTaskIdByUserIdAndDate(dailygeUser.getId(), date);
    }

    @Override
    public MonthlyTaskJpaEntity findMonthlyTaskById(final Long monthlyTaskId) {
        return monthlyTaskReadRepository.findMonthlyTaskById(monthlyTaskId)
            .orElseThrow(() -> TaskTypeException.from(MONTHLY_TASK_NOT_FOUND));
    }

    @Override
    public List<TaskJpaEntity> findTasksByMonthlyTaskIdAndDates(
        final DailygeUser dailygeUser,
        final LocalDate startDate,
        final LocalDate endDate
    ) {
        final Set<Long> findMonthlyTaskIds = monthlyTaskReadRepository.findMonthlyTasksByUserIdAndDates(dailygeUser.getId(), startDate, endDate);
        if (findMonthlyTaskIds.isEmpty()) {
            return Collections.emptyList();
        }
        return taskReadRepository.findTasksByMonthlyTaskIdAndDates(dailygeUser.getId(), findMonthlyTaskIds, startDate, endDate);
    }

    @Override
    public MonthlyTaskJpaEntity findMonthlyTaskByUserIdAndDate(
        final DailygeUser dailygeUser,
        final LocalDate date
    ) {
        final MonthlyTaskJpaEntity findMonthlyTask = monthlyTaskReadRepository.findMonthlyTaskByUserIdAndDate(dailygeUser.getId(), date)
            .orElseThrow(() -> TaskTypeException.from(MONTHLY_TASK_NOT_FOUND));
        dailygeUser.validateAuth(findMonthlyTask.getUserId());
        return findMonthlyTask;
    }

    @Override
    public List<TaskJpaEntity> findTasksByMonthlyTasksIdAndDate(
        final DailygeUser dailygeUser,
        final LocalDate date
    ) {
        final Long findMonthlyTaskId = monthlyTaskReadRepository.findMonthlyTaskIdByUserIdAndDate(dailygeUser.getId(), date);
        if (findMonthlyTaskId == null) {
            throw TaskTypeException.from(MONTHLY_TASK_NOT_FOUND);
        }
        return taskReadRepository.findMonthlyTasksByIdAndDate(findMonthlyTaskId, date);
    }

    @Override
    public Tasks findTasksStatisticByUserIdAndDate(
        final DailygeUser dailygeUser,
        final LocalDate startDate,
        final LocalDate endDate
    ) {
        final Set<Long> findMonthlyTaskIds = monthlyTaskReadRepository.findMonthlyTasksByUserIdAndDates(dailygeUser.getId(), startDate, endDate);
        if (findMonthlyTaskIds.isEmpty()) {
            return new Tasks(Collections.emptyList());
        }
        final List<TaskJpaEntity> findTasks = taskReadRepository.findTasksByMonthlyTaskIdAndDates(dailygeUser.getId(), findMonthlyTaskIds, startDate, endDate);
        return new Tasks(findTasks);
    }

    @Override
    public TaskJpaEntity findTaskById(
        final DailygeUser dailygeUser,
        final Long taskId
    ) {
        return taskReadRepository.findTaskById(taskId)
            .orElseThrow(() -> TaskTypeException.from(TASK_NOT_FOUND));
    }
}
