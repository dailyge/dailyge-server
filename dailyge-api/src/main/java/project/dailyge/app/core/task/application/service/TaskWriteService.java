package project.dailyge.app.core.task.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskWriteUseCase;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_NOT_FOUND;
import project.dailyge.app.core.task.exception.TaskTypeException;
import project.dailyge.domain.task.TaskEntityWriteRepository;
import project.dailyge.domain.task.TaskEntityReadRepository;
import project.dailyge.domain.task.TaskJpaEntity;

@Service
@RequiredArgsConstructor
class TaskWriteService implements TaskWriteUseCase {

    private final TaskValidator validator;
    private final TaskEntityReadRepository taskReadRepository;
    private final TaskEntityWriteRepository taskWriteRepository;

    @Override
    @Transactional
    public TaskJpaEntity save(final TaskJpaEntity task) {
        return taskWriteRepository.save(task);
    }

    @Override
    @Transactional
    public void delete(
        final DailygeUser dailygeUser,
        final Long taskId
    ) {
        final TaskJpaEntity findTask = taskReadRepository.findById(taskId)
            .orElseThrow(() -> TaskTypeException.from(TASK_NOT_FOUND));
        validator.validateAuth(dailygeUser, findTask);

        findTask.delete();
    }
}
