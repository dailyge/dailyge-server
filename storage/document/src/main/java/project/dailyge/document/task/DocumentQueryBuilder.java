package project.dailyge.document.task;

import org.springframework.data.mongodb.core.query.Criteria;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class DocumentQueryBuilder {

    private static final String ID = "_id";
    private static final String TASKS = "tasks";
    private static final String TASKS_STATUS = "tasks.$.status";
    private static final String TASKS_LAST_MODIFIED_BY = "tasks.$.last_modified_by";
    private static final String TASKS_LAST_MODIFIED_AT = "tasks.$.last_modified_at";

    private DocumentQueryBuilder() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static Criteria createTaskSearchCriteriaWithUserAndDate(
        final TaskDocument task,
        final LocalDate date
    ) {
        return where("user_id").is(task.getUserId())
            .and("year").is(date.getYear())
            .and("month").is(date.getMonthValue());
    }

    public static Criteria createMonthlyTaskSearchCriteriaByTaskIds(
        final String monthlyTaskId,
        final String taskId
    ) {
        return where(ID).is(monthlyTaskId)
            .and(TASKS).elemMatch(where(ID).is(taskId));
    }

    public static Update createTaskDefinition(final TaskDocument task) {
        return new Update().push(TASKS, task);
    }

    public static Update createTaskStatusUpdateDefinition(
        final Long userId,
        final String status,
        final LocalDateTime now
    ) {
        return Update.update(TASKS_STATUS, status)
            .set(TASKS_LAST_MODIFIED_BY, userId)
            .set(TASKS_LAST_MODIFIED_AT, now);
    }

    public static Update createTaskUpdateDefinition(
        final Long userId,
        final String title,
        final String content,
        final LocalDate date,
        final String status,
        final LocalDateTime now
    ) {
        return new Update()
            .set("tasks.$.title", title)
            .set("tasks.$.content", content)
            .set("tasks.$.year", date.getYear())
            .set("tasks.$.month", date.getMonthValue())
            .set("tasks.$.day", date.getDayOfMonth())
            .set("tasks.$.status", status)
            .set("tasks.$.user_id", userId)
            .set(TASKS_LAST_MODIFIED_AT, now);
    }
}
