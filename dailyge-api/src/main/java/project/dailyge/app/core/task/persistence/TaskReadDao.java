package project.dailyge.app.core.task.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.DATA_ACCESS_EXCEPTION;
import project.dailyge.app.common.exception.DaoException;
import project.dailyge.entity.task.MonthlyTaskEntityReadRepository;
import project.dailyge.entity.task.MonthlyTaskJpaEntity;
import static project.dailyge.entity.task.QMonthlyTaskJpaEntity.monthlyTaskJpaEntity;
import static project.dailyge.entity.task.QTaskJpaEntity.taskJpaEntity;
import project.dailyge.entity.task.TaskEntityReadRepository;
import project.dailyge.entity.task.TaskJpaEntity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class TaskReadDao implements TaskEntityReadRepository, MonthlyTaskEntityReadRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<TaskJpaEntity> findTaskById(final Long taskId) {
        return Optional.ofNullable(
            queryFactory.selectFrom(taskJpaEntity)
                .where(taskJpaEntity.id.eq(taskId))
                .fetchFirst()
        );
    }

    @Override
    public Optional<MonthlyTaskJpaEntity> findMonthlyTaskByUserIdAndDate(
        final Long userId,
        final LocalDate now
    ) {
        return Optional.ofNullable(
            queryFactory.selectFrom(monthlyTaskJpaEntity)
                .where(
                    monthlyTaskJpaEntity.userId.eq(userId)
                        .and(monthlyTaskJpaEntity.year.eq(now.getYear()))
                        .and(monthlyTaskJpaEntity.month.eq(now.getMonthValue()))
                )
                .fetchFirst()
        );
    }

    @Override
    public Optional<MonthlyTaskJpaEntity> findMonthlyTaskById(final Long monthlyTaskId) {
        return Optional.ofNullable(
            queryFactory.selectFrom(monthlyTaskJpaEntity)
                .where(monthlyTaskJpaEntity.id.eq(monthlyTaskId))
                .fetchFirst()
        );
    }

    @Override
    public boolean existsMonthlyPlanByUserIdAndDate(
        final Long userId,
        final LocalDate date
    ) {
        final String sql = "SELECT 1 FROM monthly_tasks WHERE user_id = ? AND year = ?";
        try {
            final Integer count = jdbcTemplate.query(sql, new Object[]{userId, date.getYear()}, rs -> {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0;
                }
            });
            return count != null && count > 0;
        } catch (DataAccessException ex) {
            throw new DaoException(ex.getMessage(), DATA_ACCESS_EXCEPTION);
        }
    }

    @Override
    public Long findMonthlyTaskIdByUserIdAndDate(
        final Long userId,
        final LocalDate date
    ) {
        return queryFactory.select(monthlyTaskJpaEntity.id)
            .from(monthlyTaskJpaEntity)
            .where(
                monthlyTaskJpaEntity.userId.eq(userId)
                    .and(monthlyTaskJpaEntity.year.eq(date.getYear()))
                    .and(monthlyTaskJpaEntity.month.eq(date.getMonthValue()))
            )
            .limit(1)
            .fetchOne();
    }

    @Override
    public List<TaskJpaEntity> findMonthlyTasksByIdAndDate(
        final Long monthlyTaskId,
        final LocalDate date
    ) {
        return queryFactory.selectFrom(taskJpaEntity)
            .where(
                taskJpaEntity.monthlyTaskId.eq(monthlyTaskId)
                    .and(taskJpaEntity.year.eq(date.getYear()))
                    .and(taskJpaEntity.year.eq(date.getYear()))
                    .and(taskJpaEntity.month.eq(date.getMonthValue()))
                    .and(taskJpaEntity.deleted.eq(false))
            ).fetch();
    }

    @Override
    public long countTodayTask(
        final Long userId,
        final LocalDate date
    ) {
        final String sql = "SELECT COUNT(*) FROM tasks WHERE user_id = ? AND date = ? AND deleted = false";
        try {
            final Date dbDate = Date.valueOf(date);
            final Long result = jdbcTemplate.queryForObject(sql, new Object[]{userId, dbDate}, Long.class);
            if (result != null) {
                return result;
            }
            return 0L;
        } catch (DataAccessException ex) {
            throw new DaoException(ex.getMessage(), DATA_ACCESS_EXCEPTION);
        }
    }
}
