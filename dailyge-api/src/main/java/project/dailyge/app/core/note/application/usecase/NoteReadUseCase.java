package project.dailyge.app.core.note.application.usecase;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.api.CursorPagingResponse;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.note.application.NoteReadService;
import static project.dailyge.app.core.note.exception.NoteCodeAndMessage.NOTE_NOT_FOUND;
import project.dailyge.app.core.note.exception.NoteTypeException;
import project.dailyge.app.paging.Cursor;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;
import static project.dailyge.entity.common.EventType.UPDATE;
import project.dailyge.entity.note.NoteEntityReadRepository;
import project.dailyge.entity.note.NoteEvent;
import project.dailyge.entity.note.NoteJpaEntity;

import java.util.List;

@ApplicationLayer(value = "NoteReadUseCase")
class NoteReadUseCase implements NoteReadService {

    private final ApplicationEventPublisher eventPublisher;
    private final NoteEntityReadRepository noteReadRepository;

    public NoteReadUseCase(
        final ApplicationEventPublisher eventPublisher,
        final NoteEntityReadRepository noteReadRepository
    ) {
        this.eventPublisher = eventPublisher;
        this.noteReadRepository = noteReadRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public NoteJpaEntity findReceivedNoteById(
        final DailygeUser dailygeUser,
        final Long noteId
    ) {
        final NoteJpaEntity findNote = noteReadRepository.findReceivedNoteById(noteId)
            .orElseThrow(() -> NoteTypeException.from(NOTE_NOT_FOUND));
        if (!findNote.validateReceiver(dailygeUser.getUserId())) {
            throw CommonException.from(UN_AUTHORIZED);
        }
        if (findNote.readByReceiver(dailygeUser.getId())) {
            final NoteEvent event = new NoteEvent(dailygeUser.getUserId(), noteId, createTimeBasedUUID(), UPDATE);
            eventPublisher.publishEvent(event);
        }
        return findNote;
    }

    @Override
    public NoteJpaEntity findSentNotesById(
        final DailygeUser dailygeUser,
        final Long noteId
    ) {
        final NoteJpaEntity findNote = noteReadRepository.findById(noteId)
            .orElseThrow(() -> NoteTypeException.from(NOTE_NOT_FOUND));
        if (!findNote.validateSender(dailygeUser.getUserId())) {
            throw CommonException.from(UN_AUTHORIZED);
        }
        return findNote;
    }

    @Override
    public CursorPagingResponse<NoteJpaEntity> findSentNotesById(
        final DailygeUser dailygeUser,
        final Cursor cursor
    ) {
        if (cursor.isNull()) {
            final List<NoteJpaEntity> findNotes = noteReadRepository.findSentNotesById(dailygeUser.getUserId(), cursor.getLimit());
            return new CursorPagingResponse<>(findNotes, cursor.getLimit());
        }
        final List<NoteJpaEntity> findNotes = noteReadRepository.findSentNotesById(dailygeUser.getUserId(), cursor.getIndex(), cursor.getLimit());
        return new CursorPagingResponse<>(findNotes, cursor.getLimit());
    }

    @Override
    public CursorPagingResponse<NoteJpaEntity> findReceivedNotesById(
        final DailygeUser dailygeUser,
        final Cursor cursor
    ) {
        if (cursor.isNull()) {
            final List<NoteJpaEntity> findNotes = noteReadRepository.findReceivedNotesById(dailygeUser.getUserId(), cursor.getLimit());
            return new CursorPagingResponse<>(findNotes, cursor.getLimit());
        }
        final List<NoteJpaEntity> findNotes = noteReadRepository.findReceivedNotesById(dailygeUser.getUserId(), cursor.getIndex(), cursor.getLimit());
        return new CursorPagingResponse<>(findNotes, cursor.getLimit());
    }

    @Override
    public List<NoteJpaEntity> findAll() {
        return noteReadRepository.findAll();
    }
}
