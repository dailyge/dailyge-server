package project.dailyge.app.core.note.persistence;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.note.NoteEntityWriteRepository;
import project.dailyge.entity.note.NoteJpaEntity;

@Repository
class NoteWriteDao implements NoteEntityWriteRepository {

    private final EntityManager entityManager;

    public NoteWriteDao(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public long save(final NoteJpaEntity note) {
        entityManager.persist(note);
        return note.getId();
    }
}
