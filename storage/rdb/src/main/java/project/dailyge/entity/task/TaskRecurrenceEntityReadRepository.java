package project.dailyge.entity.task;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRecurrenceEntityReadRepository {
    List<TaskRecurrenceJpaEntity> findByLimit(Long userId, int limit);

    List<TaskRecurrenceJpaEntity> findByCursor(Long userId, long index, int limit);

    Optional<TaskRecurrenceJpaEntity> findById(Long taskRecurrenceId);

    List<TaskJpaEntity> findTasksAfterStartDateById(Long taskRecurrenceId, LocalDate startDate);

    List<TaskJpaEntity> findTaskBeforeStartDateById(Long taskRecurrenceId, LocalDate startDate);

    Optional<TaskRecurrenceJpaEntity> findByIdAndDeleted(Long taskRecurrenceId);
}
