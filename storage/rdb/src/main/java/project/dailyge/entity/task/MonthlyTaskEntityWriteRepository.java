package project.dailyge.entity.task;

import java.util.List;

public interface MonthlyTaskEntityWriteRepository {
    Long save(MonthlyTaskJpaEntity monthlyTask);

    void saveAll(List<MonthlyTaskJpaEntity> monthlyTasks);
}
