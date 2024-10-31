package project.dailyge.app.core.emoji.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.emoji.EmojiEntityReadRepository;
import project.dailyge.entity.emoji.EmojiJpaEntity;
import static project.dailyge.entity.emoji.QEmojiJpaEntity.emojiJpaEntity;

import java.util.Optional;

@Repository
class EmojiReadDao implements EmojiEntityReadRepository {

    private final JPAQueryFactory queryFactory;

    public EmojiReadDao(final JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<EmojiJpaEntity> findById(final Long emojiId) {
        return Optional.ofNullable(
            queryFactory.selectFrom(emojiJpaEntity)
                .where(
                    emojiJpaEntity.id.eq(emojiId)
                        .and(emojiJpaEntity._deleted.eq(false))
                )
                .fetchOne()
        );
    }
}
