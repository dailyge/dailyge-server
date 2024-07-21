package project.dailyge.app.core.task.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskWriteUseCase;
import project.dailyge.app.core.task.application.command.TaskCreateCommand;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.MONTHLY_TASK_EXISTS;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_UN_RESOLVED_EXCEPTION;
import project.dailyge.app.core.task.exception.TaskTypeException;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.entity.user.UserJpaEntity;
import project.dailyge.lock.Lock;
import project.dailyge.lock.LockUseCase;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TaskFacade {

    private final LockUseCase lockUseCase;
    private final UserReadUseCase userReadUseCase;
    private final TaskWriteUseCase taskWriteUseCase;

    public void createMonthlyTasks(
        final DailygeUser dailygeUser,
        final LocalDate date
    ) {
        final Lock lock = lockUseCase.getLock(dailygeUser.getUserId());
        try {
            if (!lock.tryLock(0, 4)) {
                throw TaskTypeException.from(MONTHLY_TASK_EXISTS);
            }
            taskWriteUseCase.createMonthlyTasks(dailygeUser, date);
        } catch (InterruptedException ex) {
            throw TaskTypeException.from(ex.getMessage(), TASK_UN_RESOLVED_EXCEPTION);
        } finally {
            lockUseCase.releaseLock(lock);
        }
    }

    public String save(
        final DailygeUser dailygeUser,
        final TaskCreateCommand command
    ) {
        return taskWriteUseCase.save(dailygeUser, command);
    }

    public void delete(
        final DailygeUser dailygeUser,
        final Long taskId
    ) {
        final UserJpaEntity findUser = userReadUseCase.findActiveUserById(dailygeUser.getUserId());
        taskWriteUseCase.delete(dailygeUser, taskId);
    }
}
