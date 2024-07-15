package project.dailyge.app.core.task.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import project.dailyge.document.task.MonthlyTaskDocument;
import project.dailyge.document.task.TaskCount;
import project.dailyge.document.task.TaskDocumentReadRepository;
import static project.dailyge.entity.task.QTaskJpaEntity.taskJpaEntity;
import project.dailyge.entity.task.TaskEntityReadRepository;
import project.dailyge.entity.task.TaskJpaEntity;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class TaskReadDao implements TaskEntityReadRepository, TaskDocumentReadRepository {

    private static final String MONTHLY_DOCUMENT = "monthly_tasks";
    private static final String USER_ID = "user_id";
    private static final String YEAR = "year";
    private static final String MONTH = "month";

    private final JPAQueryFactory queryFactory;
    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<TaskJpaEntity> findById(final Long taskId) {
        return Optional.ofNullable(
            queryFactory.selectFrom(taskJpaEntity)
                .where(taskJpaEntity.id.eq(taskId))
                .fetchFirst()
        );
    }

    @Override
    public Optional<MonthlyTaskDocument> findMonthlyDocumentByUserId(
        final Long userId,
        final LocalDate date
    ) {
        final Query query = Query.query(
            Criteria.where(USER_ID).is(userId)
                .and(YEAR).is(date.getYear())
                .and(MONTH).is(date.getMonthValue())
        );
        return Optional.ofNullable(
            mongoTemplate.findOne(query, MonthlyTaskDocument.class)
        );
    }

    @Override
    public boolean existsYearPlanByUserId(
        final Long userId,
        final LocalDate date
    ) {
        final Query query = Query.query(
            Criteria.where(USER_ID).is(userId)
                .and(YEAR).is(date.getYear())
                .and(MONTH).is(date.getMonthValue())
        ).limit(1);
        return mongoTemplate.exists(query, MonthlyTaskDocument.class);
    }

    @Override
    public long countMonthlyTask(
        final Long userId,
        final LocalDate date
    ) {
        final MatchOperation matchOperation = Aggregation.match(
            Criteria.where(USER_ID).is(userId)
                .and(YEAR).is(date.getYear())
        );
        final Aggregation aggregation = Aggregation.newAggregation(
            matchOperation,
            Aggregation.group().count().as("count")
        );
        final AggregationResults<TaskCount> results = mongoTemplate.aggregate(aggregation, "monthly_tasks", TaskCount.class);
        final TaskCount taskCount = results.getUniqueMappedResult();
        return taskCount != null ? taskCount.getCount() : 0;
    }

    @Override
    public long countTodayTask(
        final Long userId,
        final LocalDate date
    ) {
        final UnwindOperation unwindOperation = Aggregation.unwind(MONTHLY_DOCUMENT);
        final MatchOperation matchOperation = Aggregation.match(
            Criteria.where(USER_ID).is(userId)
                .and(YEAR).is(date.getYear())
                .and("tasks.month").is(date.getMonthValue())
                .and("tasks.day").is(date.getDayOfMonth())
                .and("tasks.deleted").is(false)
        );
        final Aggregation aggregation = Aggregation.newAggregation(
            unwindOperation,
            matchOperation,
            Aggregation.group().count().as("count")
        );

        final AggregationResults<TaskCount> results = mongoTemplate.aggregate(aggregation, "monthly_tasks", TaskCount.class);
        final TaskCount taskCount = results.getUniqueMappedResult();
        return taskCount != null ? taskCount.getCount() : 0;
    }
}
