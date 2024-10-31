package project.dailyge.app.core.note.application.usecase;

import org.springframework.context.ApplicationEventPublisher;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.note.application.NoteReadService;
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
    public NoteJpaEntity findById(
        final DailygeUser dailygeUser,
        final Long noteId
    ) {
        final NoteJpaEntity findNote = noteReadRepository.findById(noteId)
            .orElseThrow();
        if (!findNote.validateAuth(dailygeUser.getId())) {
            throw CommonException.from(UN_AUTHORIZED);
        }
        if (findNote.readByReceiver(dailygeUser.getId())) {
            final NoteEvent event = new NoteEvent(dailygeUser.getUserId(), noteId, createTimeBasedUUID(), UPDATE);
            eventPublisher.publishEvent(event);
        }
        return findNote;
    }

    @Override
    public List<NoteJpaEntity> findAll() {
        return noteReadRepository.findAll();
    }
}
