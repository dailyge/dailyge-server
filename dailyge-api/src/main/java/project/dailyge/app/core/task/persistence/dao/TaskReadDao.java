package project.dailyge.app.core.task.persistence.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.domain.task.TaskEntityReadRepository;
import static project.dailyge.domain.task.QTaskJpaEntity.taskJpaEntity;
import project.dailyge.domain.task.TaskJpaEntity;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class TaskReadDao implements TaskEntityReadRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<TaskJpaEntity> findById(Long taskId) {
        return Optional.ofNullable(
            queryFactory.selectFrom(taskJpaEntity)
                .where(taskJpaEntity.id.eq(taskId))
                .fetchFirst()
        );
    }
}
