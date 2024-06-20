package project.dailyge.app.core.task.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.app.core.task.application.TaskReadUseCase;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_NOT_FOUND;
import project.dailyge.app.core.task.exception.TaskTypeException;
import project.dailyge.entity.task.TaskEntityReadRepository;
import project.dailyge.entity.task.TaskJpaEntity;

@Service
@RequiredArgsConstructor
class TaskReadService implements TaskReadUseCase {

    private final TaskEntityReadRepository taskRepository;

    @Override
    public TaskJpaEntity findById(
        final DailygeUser dailygeUser,
        final Long taskId
    ) {
        final TaskJpaEntity findTask = taskRepository.findById(taskId)
            .orElseThrow(() -> TaskTypeException.from(TASK_NOT_FOUND));
        if (!findTask.isOwner(dailygeUser.getUserId())) {
            throw new UnAuthorizedException();
        }
        return findTask;
    }
}
