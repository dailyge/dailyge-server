package project.dailyge.app.core.task.persistence;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
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

    private final EntityManager entityManager;
    private final MongoTemplate mongoTemplate;

    @Override
    public String save(
        final TaskDocument task,
        final LocalDate date
    ) {
        final Query query = Query.query(
            Criteria.where("user_id").is(task.getUserId())
                .and("year").is(date.getYear())
                .and("month").is(date.getMonthValue())
        );
        final Update definition = new Update().push("tasks", task);
        mongoTemplate.updateFirst(query, definition, MonthlyTaskDocument.class);
        return task.get_id();
    }

    @Override
    public void saveAll(final List<MonthlyTaskDocument> monthlyTasks) {
        mongoTemplate.insertAll(monthlyTasks);
    }

    @Override
    public TaskJpaEntity save(final TaskJpaEntity task) {
        entityManager.persist(task);
        return task;
    }
}
