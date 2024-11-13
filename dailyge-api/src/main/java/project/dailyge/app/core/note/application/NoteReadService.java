package project.dailyge.app.core.note.application;

import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.paging.Cursor;
import project.dailyge.entity.note.NoteJpaEntity;

import java.util.List;

public interface NoteReadService {
    NoteJpaEntity findReceivedNoteById(DailygeUser dailygeUser, Long noteId);

    NoteJpaEntity findSentNotesById(DailygeUser dailygeUser, Long noteId);

    List<NoteJpaEntity> findSentNotesById(DailygeUser dailygeUser, Cursor cursor);

    List<NoteJpaEntity> findReceivedNotesById(DailygeUser dailygeUser, Cursor cursor);

    List<NoteJpaEntity> findAll();
}
