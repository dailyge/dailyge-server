package project.dailyge.app.core.task.persistence.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.app.core.task.persistence.TaskEntityWriteRepository;
import project.dailyge.domain.task.TaskJpaEntity;
import project.dailyge.domain.task.TaskJpaRepository;

@Repository
@RequiredArgsConstructor
public class TaskWriteDao implements TaskEntityWriteRepository {

    private final TaskJpaRepository taskRepository;

    @Override
    public TaskJpaEntity save(final TaskJpaEntity task) {
        return taskRepository.save(task);
    }
}
