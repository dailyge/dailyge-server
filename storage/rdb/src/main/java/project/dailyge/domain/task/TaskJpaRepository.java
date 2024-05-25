package project.dailyge.domain.task;

import org.springframework.data.jpa.repository.*;

public interface TaskJpaRepository extends JpaRepository<Task, Long> {
}
