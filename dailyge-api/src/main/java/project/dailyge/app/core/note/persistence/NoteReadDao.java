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

    @Override
    public Optional<NoteJpaEntity> findById(final Long noteId) {
        return Optional.ofNullable(queryFactory.selectFrom(noteJpaEntity)
            .where(
                noteJpaEntity.id.eq(noteId)
                    .and(noteJpaEntity._deleted.eq(false))
            )
            .fetchOne());
    }

    @Override
    public List<NoteJpaEntity> findAll() {
        return queryFactory.selectFrom(noteJpaEntity)
            .fetch();
    }
}
