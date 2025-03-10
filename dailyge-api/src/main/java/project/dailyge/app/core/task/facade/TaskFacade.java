package project.dailyge.app.core.task.facade;

import project.dailyge.app.common.annotation.FacadeLayer;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskWriteService;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.MONTHLY_TASK_EXISTS;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_UN_RESOLVED_EXCEPTION;
import project.dailyge.app.core.task.exception.TaskTypeException;
import static project.dailyge.entity.user.Role.NORMAL;
import project.dailyge.lock.Lock;
import project.dailyge.lock.LockService;

import java.time.LocalDate;

@FacadeLayer(value = "TaskFacade")
public class TaskFacade {

    private final LockService lockService;
    private final TaskWriteService taskWriteService;

    public TaskFacade(
        final LockService lockService,
        final TaskWriteService taskWriteService
    ) {
        this.lockService = lockService;
        this.taskWriteService = taskWriteService;
    }

    public void createMonthlyTasks(
        final DailygeUser dailygeUser,
        final LocalDate date
    ) {
        final Lock lock = lockService.getLock(dailygeUser.getUserId());
        try {
            if (!lock.tryLock(0, 4)) {
                throw TaskTypeException.from(MONTHLY_TASK_EXISTS);
            }
            taskWriteService.saveAll(dailygeUser, date);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw TaskTypeException.from(ex.getMessage(), TASK_UN_RESOLVED_EXCEPTION);
        } finally {
            lockService.releaseLock(lock);
        }
    }

    public void createMonthlyTasksInternally(
        final Long userId,
        final LocalDate date
    ) {
        final Lock lock = lockService.getLock(userId);
        try {
            if (!lock.tryLock(0, 4)) {
                throw TaskTypeException.from(MONTHLY_TASK_EXISTS);
            }
            taskWriteService.saveAll(new DailygeUser(userId, NORMAL), date);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw TaskTypeException.from(ex.getMessage(), TASK_UN_RESOLVED_EXCEPTION);
        } finally {
            lockService.releaseLock(lock);
        }
    }
}
