package project.dailyge.app.core.note.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.note.NoteEntityReadRepository;
import project.dailyge.entity.note.NoteJpaEntity;
import static project.dailyge.entity.note.QNoteJpaEntity.noteJpaEntity;

import java.util.List;
import java.util.Optional;

@Repository
class NoteReadDao implements NoteEntityReadRepository {

    private final JPAQueryFactory queryFactory;

    public NoteReadDao(final JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 받은 쪽지함 삭제
     */
    @Override
    public Optional<NoteJpaEntity> findSentNoteById(
        final Long userId,
        final Long noteId
    ) {
        return Optional.ofNullable(
            queryFactory.selectFrom(noteJpaEntity)
                .where(
                    noteJpaEntity.id.eq(noteId)
                        .and(noteJpaEntity.senderId.eq(userId))
                        .and(noteJpaEntity._deleted.eq(false))
                )
                .fetchOne());
    }

    /**
     * 보낸 쪽지함 삭제
     */
    @Override
    public Optional<NoteJpaEntity> findById(final Long noteId) {
        return Optional.ofNullable(
            queryFactory.selectFrom(noteJpaEntity)
                .where(
                    noteJpaEntity.id.eq(noteId)
                        .and(noteJpaEntity._senderDeleted.eq(false))
                )
                .fetchOne()
        );
    }

    @Override
    public Optional<NoteJpaEntity> findReceivedNoteById(
        final Long userId,
        final Long noteId
    ) {
        return Optional.ofNullable(
            queryFactory.selectFrom(noteJpaEntity)
                .where(
                    noteJpaEntity.id.eq(noteId)
                        .and(noteJpaEntity.receiverId.eq(userId))
                        .and(noteJpaEntity._receiverDeleted.eq(false))
                )
                .fetchOne());
    }

    /**
     * Method created for testing purposes; should not be called in a production environment.
     * <p>
     * <b>Warning:</b> This method is intended for testing use only and should not be used in production.
     * </p>
     *
     * @return a list of all {@link NoteJpaEntity} entities
     */
    @Override
    public List<NoteJpaEntity> findAll() {
        return queryFactory.selectFrom(noteJpaEntity)
            .fetch();
    }
}
