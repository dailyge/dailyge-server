package project.dailyge.app.core.task.persistence;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import project.dailyge.document.task.MonthlyTaskDocument;
import project.dailyge.document.task.TaskActivity;
import project.dailyge.document.task.TaskCount;
import project.dailyge.document.task.TaskDocument;
import project.dailyge.document.task.TaskDocumentReadRepository;
import static project.dailyge.entity.task.QTaskJpaEntity.taskJpaEntity;
import project.dailyge.entity.task.TaskEntityReadRepository;
import project.dailyge.entity.task.TaskJpaEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class TaskReadDao implements TaskEntityReadRepository, TaskDocumentReadRepository {

    private static final String MONTHLY_TASKS_DOCUMENT = "monthly_tasks";
    private static final String TASKS = "tasks";
    private static final String TASKS_ID = "tasks._id";
    private static final String TASKS_USER_ID = "tasks.user_id";
    private static final String COUNT = "count";
    private static final String ID = "_id";
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

    /**
     * findOne 이슈로 인해, 저수준 Collection 직접 조회.
     */
    @Override
    public Optional<MonthlyTaskDocument> findMonthlyTaskById(final String monthlyTaskId) {
        final MongoCollection<Document> collection = mongoTemplate.getCollection(MONTHLY_TASKS_DOCUMENT);
        final Document doc = collection.find(Filters.eq(ID, monthlyTaskId)).first();
        return Optional.ofNullable(
            mongoTemplate.getConverter().read(MonthlyTaskDocument.class, doc)
        );
    }

    @Override
    public Optional<TaskDocument> findTaskDocumentByIds(
        final String monthlyTaskId,
        final String taskId
    ) {
        final MongoCollection<Document> collection = mongoTemplate.getCollection(MONTHLY_TASKS_DOCUMENT);

        final Document document = collection.aggregate(Arrays.asList(
            Aggregates.match(Filters.eq(ID, monthlyTaskId)),
            Aggregates.unwind("$tasks"),
            Aggregates.match(Filters.eq("tasks._id", taskId)),
            Aggregates.project(Projections.fields(Projections.excludeId(), Projections.include(TASKS)))
        )).first();
        if (document != null && document.get(TASKS) != null) {
            final Document task = (Document) document.get(TASKS);
            return Optional.of(mongoTemplate.getConverter().read(TaskDocument.class, task));
        }
        return Optional.empty();
    }

    @Override
    public Optional<TaskActivity> findTaskDocumentByIds(
        final Long userId,
        final String monthlyTaskId,
        final String taskId
    ) {
        final MatchOperation monthlyTaskMatch = Aggregation.match(Criteria.where(ID).is(monthlyTaskId));
        final UnwindOperation unwindTasks = Aggregation.unwind(TASKS);
        final MatchOperation matchTask = Aggregation.match(
            Criteria.where(TASKS_USER_ID).is(userId)
                .and(TASKS_ID).is(taskId)
        );
        final ProjectionOperation project = Aggregation.project()
            .and(TASKS_USER_ID).as("userId")
            .and(TASKS_ID).as("taskId")
            .andExclude(ID);

        final Aggregation aggregation = Aggregation.newAggregation(
            monthlyTaskMatch,
            unwindTasks,
            matchTask,
            project
        );

        final AggregationResults<TaskActivity> result = mongoTemplate.aggregate(
            aggregation, MONTHLY_TASKS_DOCUMENT, TaskActivity.class
        );
        final List<TaskActivity> taskActivities = result.getMappedResults();
        return taskActivities.isEmpty() ? Optional.empty() : Optional.of(taskActivities.get(0));
    }

    @Override
    public Optional<MonthlyTaskDocument> findMonthlyDocumentByUserIdAndDate(
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
    public boolean existsMonthlyPlanByUserIdAndDate(
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
            Aggregation.group().count().as(COUNT)
        );
        final AggregationResults<TaskCount> results = mongoTemplate.aggregate(
            aggregation, MONTHLY_TASKS_DOCUMENT, TaskCount.class
        );
        final TaskCount taskCount = results.getUniqueMappedResult();
        return taskCount != null ? taskCount.getCount() : 0;
    }

    @Override
    public long countTodayTask(
        final Long userId,
        final LocalDate date
    ) {
        final UnwindOperation unwindOperation = Aggregation.unwind(MONTHLY_TASKS_DOCUMENT);
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
            Aggregation.group().count().as(COUNT)
        );

        final AggregationResults<TaskCount> results = mongoTemplate.aggregate(
            aggregation, MONTHLY_TASKS_DOCUMENT, TaskCount.class
        );
        final TaskCount taskCount = results.getUniqueMappedResult();
        return taskCount != null ? taskCount.getCount() : 0;
    }
}
