package project.dailyge.app.core.note.application;

import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.entity.note.NoteEvent;
import project.dailyge.entity.note.NoteJpaEntity;

public interface NoteWriteService {
    Long save(DailygeUser dailygeUser, NoteJpaEntity note);

    void update(NoteEvent event);

    void deleteReceivedNoteById(DailygeUser dailygeUser, Long noteId);

    void deleteSentNoteById(DailygeUser dailygeUser, Long noteId);
}
