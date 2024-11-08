package project.dailyge.app.core.note.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import project.dailyge.app.core.note.application.NoteWriteService;
import static project.dailyge.entity.common.EventType.UPDATE;
import project.dailyge.entity.note.NoteEvent;

@Component
public class NoteEventListener {

    private final NoteWriteService noteWriteService;

    public NoteEventListener(final NoteWriteService noteWriteService) {
        this.noteWriteService = noteWriteService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void listen(final NoteEvent event) {
        if (event.isType(UPDATE)) {
            noteWriteService.update(event);
        }
    }
}
