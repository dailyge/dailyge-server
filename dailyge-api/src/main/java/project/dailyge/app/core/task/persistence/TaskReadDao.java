package project.dailyge.app.core.task.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.task.TaskEntityReadRepository;
import static project.dailyge.entity.task.QTaskJpaEntity.taskJpaEntity;
import project.dailyge.entity.task.TaskJpaEntity;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class TaskReadDao implements TaskEntityReadRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<TaskJpaEntity> findById(final Long taskId) {
        return Optional.ofNullable(
            queryFactory.selectFrom(taskJpaEntity)
                .where(taskJpaEntity.id.eq(taskId))
                .fetchFirst()
        );
    }
}
