package project.dailyge.app.core.task.persistence.dao;

import lombok.*;
import org.springframework.stereotype.*;
import project.dailyge.app.core.task.persistence.*;
import project.dailyge.domain.task.*;

@Repository
@RequiredArgsConstructor
public class TaskWriteDao implements TaskEntityWriteRepository {

    private final TaskJpaRepository taskRepository;

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }
}
