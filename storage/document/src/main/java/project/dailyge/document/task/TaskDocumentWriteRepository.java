package project.dailyge.document.task;

import java.time.LocalDate;

public interface TaskDocumentWriteRepository {
    String save(TaskDocument task, LocalDate date);

    void update(Long userId, String monthlyTaskId, String taskId, String title, String content, LocalDate date, String status);

    void update(Long userId, String monthlyTaskId, String taskId, String status);
}
