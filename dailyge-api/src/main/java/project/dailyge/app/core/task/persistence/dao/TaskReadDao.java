package project.dailyge.app.core.task.persistence.dao;

import com.querydsl.jpa.impl.*;
import lombok.*;
import org.springframework.stereotype.*;
import project.dailyge.app.core.task.persistence.*;
import project.dailyge.domain.task.*;
import static project.dailyge.domain.task.QTask.task;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class TaskReadDao implements TaskEntityReadRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<TaskJpaEntity> findById(Long taskId) {
        return Optional.ofNullable(
            queryFactory.selectFrom(task)
                .where(task.id.eq(taskId))
                .fetchFirst()
        );
    }
}
