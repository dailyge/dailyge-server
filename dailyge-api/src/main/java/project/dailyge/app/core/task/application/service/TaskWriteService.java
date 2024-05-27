package project.dailyge.app.core.task.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.core.task.application.TaskWriteUseCase;
import project.dailyge.app.core.task.persistence.TaskEntityWriteRepository;
import project.dailyge.domain.task.TaskJpaEntity;

@Service
@RequiredArgsConstructor
public class TaskWriteService implements TaskWriteUseCase {

    private final TaskEntityWriteRepository taskRepository;

    @Override
    @Transactional
    public TaskJpaEntity save(final TaskJpaEntity task) {
        return taskRepository.save(task);
    }
}
