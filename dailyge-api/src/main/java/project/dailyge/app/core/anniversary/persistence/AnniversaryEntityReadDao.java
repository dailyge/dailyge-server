package project.dailyge.app.core.anniversary.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.anniversary.AnniversaryEntityReadRepository;
import project.dailyge.entity.anniversary.AnniversaryJpaEntity;
import static project.dailyge.entity.anniversary.QAnniversaryJpaEntity.anniversaryJpaEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
class AnniversaryEntityReadDao implements AnniversaryEntityReadRepository {

    private final JPAQueryFactory queryFactory;

    public AnniversaryEntityReadDao(final JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<AnniversaryJpaEntity> findById(final Long anniversaryId) {
        return Optional.ofNullable(
            queryFactory.selectFrom(anniversaryJpaEntity)
                .where(anniversaryJpaEntity.id.eq(anniversaryId)
                    .and(anniversaryJpaEntity._deleted.eq(false))
                )
                .fetchOne()
        );
    }

    @Override
    public boolean exists(
        final String name,
        final LocalDateTime date
    ) {
        final Integer findAnniversary = queryFactory.selectOne()
            .from(anniversaryJpaEntity)
            .where(anniversaryJpaEntity._name.eq(name)
                .and(anniversaryJpaEntity._date.eq(date))
                .and(anniversaryJpaEntity._deleted.eq(false))
            ).fetchFirst();
        return findAnniversary != null;
    }

    @Override
    public List<AnniversaryJpaEntity> findByDates(
        final Long userId,
        final LocalDate startDate,
        final LocalDate endDate
    ) {
        return queryFactory.selectFrom(anniversaryJpaEntity)
            .where(anniversaryJpaEntity.userId.eq(userId)
                .and(anniversaryJpaEntity._date.between(convertLocalDateTime(startDate), convertLocalDateTime(endDate)))
            )
            .fetch();
    }

    private LocalDateTime convertLocalDateTime(final LocalDate date) {
        return date.atTime(0, 0, 0, 0);
    }
}
