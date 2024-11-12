package project.dailyge.entity.note;

import java.util.List;
import java.util.Optional;

public interface NoteEntityReadRepository {
    Optional<NoteJpaEntity> findById(Long noteId);

    Optional<NoteJpaEntity> findReceivedNoteById(Long noteId);

    Optional<NoteJpaEntity> findSentNoteById(Long noteId);

    List<NoteJpaEntity> findSentNotesById(Long userId, Integer size);

    List<NoteJpaEntity> findSentNotesById(Long userId, Long index, Integer size);

    List<NoteJpaEntity> findReceivedNotesById(Long userId, Integer size);

    List<NoteJpaEntity> findReceivedNotesById(Long userId, Long index, Integer size);

    List<NoteJpaEntity> findAll();
}
