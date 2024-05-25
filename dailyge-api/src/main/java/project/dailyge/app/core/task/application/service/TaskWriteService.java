package project.dailyge.app.core.task.application.service;

import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import project.dailyge.app.core.task.application.*;
import project.dailyge.app.core.task.persistence.*;
import project.dailyge.domain.task.*;

@Service
@RequiredArgsConstructor
public class TaskWriteService implements TaskWriteUseCase {

    private final TaskEntityWriteRepository taskRepository;

    @Override
    @Transactional
    public Task save(Task task) {
        return taskRepository.save(task);
    }
}
