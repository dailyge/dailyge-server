package project.dailyge.app.core.retrospect.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.retrospect.RetrospectEntityReadRepository;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;
import static java.util.Optional.ofNullable;
import static project.dailyge.entity.retrospect.QRetrospectJpaEntity.retrospectJpaEntity;

@Repository
@RequiredArgsConstructor
class RetrospectEntityReadDao implements RetrospectEntityReadRepository {

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
}
