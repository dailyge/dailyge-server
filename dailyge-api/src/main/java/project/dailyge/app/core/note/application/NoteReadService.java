package project.dailyge.app.core.note.application;

import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.entity.note.NoteJpaEntity;

import java.util.List;

public interface NoteReadService {
    NoteJpaEntity findById(DailygeUser dailygeUser, Long noteId);

    NoteJpaEntity findReceivedNoteById(DailygeUser dailygeUser, Long noteId);

    NoteJpaEntity findSentNoteById(DailygeUser dailygeUser, Long noteId);

    List<NoteJpaEntity> findAll();
}
