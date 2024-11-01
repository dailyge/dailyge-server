package project.dailyge.app.core.note.application.usecase;

import static java.time.LocalDateTime.now;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.note.application.NoteWriteService;
import static project.dailyge.app.core.note.exception.NoteCodeAndMessage.NOTE_NOT_FOUND;
import project.dailyge.app.core.note.exception.NoteTypeException;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;
import static project.dailyge.entity.common.EventType.CREATE;
import project.dailyge.entity.note.NoteEntityReadRepository;
import project.dailyge.entity.note.NoteEntityWriteRepository;
import project.dailyge.entity.note.NoteEvent;
import project.dailyge.entity.note.NoteJpaEntity;

@ApplicationLayer(value = "NoteWriteUseCase")
class NoteWriteUseCase implements NoteWriteService {

    private final NoteValidator validator;
    private final NoteEntityReadRepository noteReadRepository;
    private final NoteEntityWriteRepository noteWriteRepository;
    private final ApplicationEventPublisher eventPublisher;

    public NoteWriteUseCase(
        final NoteValidator validator,
        final NoteEntityReadRepository noteReadRepository,
        final NoteEntityWriteRepository noteWriteRepository,
        final ApplicationEventPublisher eventPublisher
    ) {
        this.validator = validator;
        this.noteReadRepository = noteReadRepository;
        this.noteWriteRepository = noteWriteRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Long save(
        final DailygeUser dailygeUser,
        final NoteJpaEntity note
    ) {
        validator.validate(dailygeUser.getId(), note.getReceiverId());
        final long newNoteId = noteWriteRepository.save(note);
        final NoteEvent event = new NoteEvent(dailygeUser.getUserId(), newNoteId, createTimeBasedUUID(), CREATE);
        eventPublisher.publishEvent(event);
        return newNoteId;
    }

    @Override
    @Transactional
    public void update(final NoteEvent event) {
        final NoteJpaEntity findNote = noteReadRepository.findById(event.getNoteId())
            .orElseThrow(() -> NoteTypeException.from(NOTE_NOT_FOUND));
        findNote.updateReadStatus(true, event.getCreatedAt());
    }

    /**
     * 받은 쪽지함 삭제.
     */
    @Override
    @Transactional
    public void deleteReceivedNoteById(
        final DailygeUser dailygeUser,
        final Long noteId
    ) {
        final NoteJpaEntity findNote = noteReadRepository.findReceivedNoteById(dailygeUser.getId(), noteId)
            .orElseThrow(() -> NoteTypeException.from(NOTE_NOT_FOUND));
        if (!findNote.validateReceiver(dailygeUser.getUserId())) {
            throw CommonException.from(UN_AUTHORIZED);
        }
        findNote.delete(dailygeUser.getId(), now());
    }

    /**
     * 보낸 쪽지함 삭제.
     */
    @Override
    @Transactional
    public void deleteSentNoteById(
        final DailygeUser dailygeUser,
        final Long noteId
    ) {
        final NoteJpaEntity findNote = noteReadRepository.findSentNoteById(dailygeUser.getId(), noteId)
            .orElseThrow(() -> NoteTypeException.from(NOTE_NOT_FOUND));
        if (!findNote.validateSender(dailygeUser.getUserId())) {
            throw CommonException.from(UN_AUTHORIZED);
        }
        findNote.delete(dailygeUser.getId(), now());
    }
}
