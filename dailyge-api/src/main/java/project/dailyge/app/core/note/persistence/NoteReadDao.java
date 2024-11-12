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
    public Optional<NoteJpaEntity> findSentNoteById(final Long noteId) {
        return Optional.ofNullable(
            queryFactory.selectFrom(noteJpaEntity)
                .where(
                    noteJpaEntity.id.eq(noteId)
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
    public Optional<NoteJpaEntity> findReceivedNoteById(final Long noteId) {
        return Optional.ofNullable(
            queryFactory.selectFrom(noteJpaEntity)
                .where(
                    noteJpaEntity.id.eq(noteId)
                        .and(noteJpaEntity._receiverDeleted.eq(false))
                )
                .fetchOne());
    }

    @Override
    public List<NoteJpaEntity> findSentNotesById(
        final Long userId,
        final Integer size
    ) {
        return queryFactory.selectFrom(noteJpaEntity)
            .where(
                noteJpaEntity.senderId.eq(userId)
                    .and(noteJpaEntity._senderDeleted.eq(false))
            )
            .limit(size + 1)
            .orderBy(noteJpaEntity.id.desc())
            .fetch();
    }

    @Override
    public List<NoteJpaEntity> findSentNotesById(
        final Long userId,
        final Long index,
        final Integer size
    ) {
        return queryFactory.selectFrom(noteJpaEntity)
            .where(
                noteJpaEntity.senderId.eq(userId)
                    .and(noteJpaEntity.id.lt(index))
                    .and(noteJpaEntity._senderDeleted.eq(false))
            )
            .limit(size + 1)
            .orderBy(noteJpaEntity.id.desc())
            .fetch();
    }

    @Override
    public List<NoteJpaEntity> findReceivedNotesById(
        final Long userId,
        final Integer size
    ) {
        return queryFactory.selectFrom(noteJpaEntity)
            .where(
                noteJpaEntity.receiverId.eq(userId)
                    .and(noteJpaEntity._receiverDeleted.eq(false))
            )
            .limit(size + 1)
            .orderBy(noteJpaEntity.id.desc())
            .fetch();
    }

    @Override
    public List<NoteJpaEntity> findReceivedNotesById(
        final Long userId,
        final Long index,
        final Integer size
    ) {
        return queryFactory.selectFrom(noteJpaEntity)
            .where(
                noteJpaEntity.receiverId.eq(userId)
                    .and(noteJpaEntity.id.lt(index))
                    .and(noteJpaEntity._receiverDeleted.eq(false))
            )
            .limit(size + 1)
            .orderBy(noteJpaEntity.id.desc())
            .fetch();
    }

    @Override
    public List<NoteJpaEntity> findAll() {
        return queryFactory.selectFrom(noteJpaEntity)
            .fetch();
    }
}
