package project.dailyge.document.task;

import java.time.LocalDate;
import java.util.List;

public interface TaskDocumentWriteRepository {
    String save(TaskDocument task, LocalDate date);

    void saveAll(List<MonthlyTaskDocument> monthlyTasks);
}
