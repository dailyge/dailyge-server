package project.dailyge.entity.note;

import java.util.List;
import java.util.Optional;

public interface NoteEntityReadRepository {
    Optional<NoteJpaEntity> findById(Long noteId);

    Optional<NoteJpaEntity> findReceivedNoteById(Long noteId);

    Optional<NoteJpaEntity> findSentNoteById(Long noteId);

    List<NoteJpaEntity> findAll();
}
