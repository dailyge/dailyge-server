package project.dailyge.entity.note;

import java.util.List;
import java.util.Optional;

public interface NoteEntityReadRepository {
    Optional<NoteJpaEntity> findById(Long noteId);

    List<NoteJpaEntity> findAll();
}
