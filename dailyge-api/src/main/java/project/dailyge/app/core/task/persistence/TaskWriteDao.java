package project.dailyge.app.core.task.persistence;

import jakarta.persistence.EntityManager;
import static java.time.LocalDateTime.now;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import static project.dailyge.document.task.DocumentQueryBuilder.createMonthlyTaskSearchCriteriaByTaskIds;
import static project.dailyge.document.task.DocumentQueryBuilder.createTaskDefinition;
import static project.dailyge.document.task.DocumentQueryBuilder.createTaskSearchCriteriaWithUserAndDate;
import static project.dailyge.document.task.DocumentQueryBuilder.createTaskStatusUpdateDefinition;
import static project.dailyge.document.task.DocumentQueryBuilder.createTaskUpdateDefinition;
import project.dailyge.document.task.MonthlyTaskDocument;
import project.dailyge.document.task.TaskDocument;
import project.dailyge.document.task.TaskDocumentWriteRepository;
import project.dailyge.entity.task.TaskEntityWriteRepository;
import project.dailyge.entity.task.TaskJpaEntity;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
class TaskWriteDao implements TaskEntityWriteRepository, TaskDocumentWriteRepository {

    private static final String MONTHLY_TASKS = "monthly_tasks";
    private static final String TASKS = "tasks";
    private static final String ID = "_id";

    private final EntityManager entityManager;
    private final MongoTemplate mongoTemplate;

    @Override
    public String save(
        final TaskDocument task,
        final LocalDate date
    ) {
        final Criteria criteria = createTaskSearchCriteriaWithUserAndDate(task, date);
        final Query query = Query.query(criteria);
        final Update definition = createTaskDefinition(task);
        mongoTemplate.updateFirst(query, definition, MonthlyTaskDocument.class);
        return task.getId();
    }

    @Override
    public TaskJpaEntity save(final TaskJpaEntity task) {
        entityManager.persist(task);
        return task;
    }

    @Override
    public void saveAll(final List<MonthlyTaskDocument> monthlyTasks) {
        mongoTemplate.insertAll(monthlyTasks);
    }

    @Override
    public void update(
        final Long userId,
        final String monthlyTaskId,
        final String taskId,
        final String status
    ) {
        final Criteria criteria = createMonthlyTaskSearchCriteriaByTaskIds(monthlyTaskId, taskId);
        final Query query = new Query(criteria);
        final Update definition = createTaskStatusUpdateDefinition(userId, status, now());
        mongoTemplate.updateFirst(query, definition, MONTHLY_TASKS);
    }

    @Override
    public void update(
        final Long userId,
        final String monthlyTaskId,
        final String taskId,
        final String title,
        final String content,
        final LocalDate date,
        final String status
    ) {
        final Query query = Query.query(
            where(ID).is(monthlyTaskId)
                .and(TASKS).elemMatch(where(ID).is(taskId))
        );
        final Update definition = createTaskUpdateDefinition(userId, title, content, date, status, now());
        mongoTemplate.updateFirst(query, definition, MONTHLY_TASKS);
    }
}
