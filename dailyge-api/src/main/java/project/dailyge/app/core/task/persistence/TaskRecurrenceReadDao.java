package project.dailyge.app.core.task.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import project.dailyge.app.paging.Cursor;
import project.dailyge.entity.task.TaskJpaEntity;
import project.dailyge.entity.task.TaskRecurrenceEntityReadRepository;
import project.dailyge.entity.task.TaskRecurrenceJpaEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static project.dailyge.entity.task.QTaskJpaEntity.taskJpaEntity;
import static project.dailyge.entity.task.QTaskRecurrenceJpaEntity.taskRecurrenceJpaEntity;

@Repository
public class TaskRecurrenceReadDao implements TaskRecurrenceEntityReadRepository {

    private final JPAQueryFactory queryFactory;

    public TaskRecurrenceReadDao(final JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<TaskRecurrenceJpaEntity> findByLimit(
        final Long userId,
        final int limit
    ) {
        return queryFactory.selectFrom(taskRecurrenceJpaEntity)
            .where(
                taskRecurrenceJpaEntity.userId.eq(userId)
                    .and(taskRecurrenceJpaEntity._deleted.eq(false))
            )
            .orderBy(taskRecurrenceJpaEntity._createdAt.asc())
            .limit(limit)
            .fetch();
    }

    @Override
    public List<TaskRecurrenceJpaEntity> findByCursor(
        final Long userId,
        final long index,
        final int limit
    ) {
        return queryFactory.selectFrom(taskRecurrenceJpaEntity)
            .where(
                taskRecurrenceJpaEntity.userId.eq(userId)
                    .and(taskRecurrenceJpaEntity.id.gt(index))
                    .and(taskRecurrenceJpaEntity._deleted.eq(false))
            )
            .orderBy(taskRecurrenceJpaEntity._createdAt.asc())
            .limit(limit)
            .fetch();
    }

    public List<TaskRecurrenceJpaEntity> findByCursor(
        final Long userId,
        final Cursor cursor
    ) {
        if (cursor.isNull()) {
            return findByLimit(userId, cursor.getLimit());
        }
        return findByCursor(userId, cursor.getIndex(), cursor.getLimit());
    }

    @Override
    public Optional<TaskRecurrenceJpaEntity> findById(final Long taskRecurrenceId) {
        return Optional.ofNullable(
            queryFactory.selectFrom(taskRecurrenceJpaEntity)
                .where(
                    taskRecurrenceJpaEntity.id.eq(taskRecurrenceId)
                        .and(taskRecurrenceJpaEntity._deleted.eq(false))
                )
                .fetchFirst()
        );
    }

    @Override
    public List<TaskJpaEntity> findTasksAfterStartDateById(
        final Long taskRecurrenceId,
        final LocalDate startDate
    ) {
        return queryFactory.selectFrom(taskJpaEntity)
            .where(
                taskJpaEntity.taskRecurrenceId.eq(taskRecurrenceId)
                    .and(taskJpaEntity._deleted.eq(false))
                    .and(taskJpaEntity.date.before(startDate).not())
            )
            .fetch();
    }

    @Override
    public List<TaskJpaEntity> findTaskBeforeStartDateById(
        final Long taskRecurrenceId,
        final LocalDate startDate
    ) {
        return queryFactory.selectFrom(taskJpaEntity)
            .where(
                taskJpaEntity.taskRecurrenceId.eq(taskRecurrenceId)
                    .and(taskJpaEntity._deleted.eq(false))
                    .and(taskJpaEntity.date.before(startDate))
            )
            .fetch();
    }
}
