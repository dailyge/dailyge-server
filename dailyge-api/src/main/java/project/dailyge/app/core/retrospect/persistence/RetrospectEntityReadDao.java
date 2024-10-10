package project.dailyge.app.core.retrospect.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.app.page.Page;
import project.dailyge.entity.retrospect.RetrospectEntityReadRepository;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;
import static java.util.Optional.ofNullable;
import static project.dailyge.entity.retrospect.QRetrospectJpaEntity.retrospectJpaEntity;

@Repository
@RequiredArgsConstructor
public class RetrospectEntityReadDao implements RetrospectEntityReadRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<RetrospectJpaEntity> findById(final Long retrospectId) {
        return ofNullable(jpaQueryFactory.selectFrom(retrospectJpaEntity)
            .where(
                retrospectJpaEntity.id.eq(retrospectId)
                    .and(retrospectJpaEntity.deleted.eq(false))
            )
            .fetchOne()
        );
    }

    public List<RetrospectJpaEntity> findRetrospectByPage(
        final Long userId,
        final Page page
    ) {
        return jpaQueryFactory.selectFrom(retrospectJpaEntity)
            .where(
                retrospectJpaEntity.id.gt(page.getOffset())
                .and(retrospectJpaEntity.userId.eq(userId))
                .and(retrospectJpaEntity.deleted.eq(false))
            )
            .limit(page.getLimit())
            .fetch();
    }
}
