package project.dailyge.app.core.retrospect.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.paging.CustomPageable;
import project.dailyge.app.response.AsyncPagingResponse;
import project.dailyge.entity.retrospect.RetrospectEntityReadRepository;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;
import static java.util.Optional.ofNullable;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.DATA_ACCESS_EXCEPTION;
import static project.dailyge.entity.retrospect.QRetrospectJpaEntity.retrospectJpaEntity;

@Repository
@RequiredArgsConstructor
public class RetrospectEntityReadDao implements RetrospectEntityReadRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final JdbcTemplate jdbcTemplate;

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

    public AsyncPagingResponse<RetrospectJpaEntity> findRetrospectAndTotalCountByPage(
        final Long userId,
        final CustomPageable page
    ) {
        final CompletableFuture<List<RetrospectJpaEntity>> retrospectsFuture = CompletableFuture.supplyAsync(() ->
            this.findRetrospectByPage(userId, page)
        );
        final CompletableFuture<Integer> totalCountFuture = CompletableFuture.supplyAsync(() ->
            this.findTotalCount(userId)
        );

        final CompletableFuture<AsyncPagingResponse> asyncPagingResponseFuture = CompletableFuture.allOf(retrospectsFuture, totalCountFuture)
            .thenApply(ignored -> {
                final List<RetrospectJpaEntity> retrospects = retrospectsFuture.join();
                final int totalCount = totalCountFuture.join();
                return new AsyncPagingResponse(retrospects, totalCount);
            }).exceptionally(ex -> {
                throw CommonException.from(ex.getMessage(), DATA_ACCESS_EXCEPTION);
            });
        return asyncPagingResponseFuture.join();
    }

    private List<RetrospectJpaEntity> findRetrospectByPage(
        final Long userId,
        final CustomPageable page
    ) {
        return jpaQueryFactory.selectFrom(retrospectJpaEntity)
            .where(
                retrospectJpaEntity.userId.eq(userId)
                    .and(retrospectJpaEntity.deleted.eq(false))
            )
            .orderBy(retrospectJpaEntity.date.desc())
            .offset(page.getOffset())
            .limit(page.getPageSize())
            .fetch();
    }

    private int findTotalCount(final Long userId) {
        final String sql = "SELECT count(id) FROM retrospects WHERE user_id = ? AND deleted = false";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, userId);
        } catch (DataAccessException ex) {
            return 0;
        }
    }
}
