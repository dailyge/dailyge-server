package project.dailyge.app.core.note.facade;

import static java.lang.Thread.currentThread;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.TOO_MANY_REQUEST;
import project.dailyge.app.common.annotation.FacadeLayer;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.note.application.NoteWriteService;
import project.dailyge.app.core.note.application.command.NoteCreateCommand;
import static project.dailyge.app.core.note.exception.NoteCodeAndMessage.NOTE_UN_RESOLVED_EXCEPTION;
import project.dailyge.app.core.note.exception.NoteTypeException;
import project.dailyge.app.core.user.application.UserReadService;
import project.dailyge.entity.user.UserJpaEntity;
import project.dailyge.lock.Lock;
import project.dailyge.lock.LockService;

@FacadeLayer(value = "NoteFacade")
public class NoteFacade {

    private final LockService lockService;
    private final UserReadService userReadService;
    private final NoteWriteService noteWriteService;

    public NoteFacade(
        final LockService lockService,
        final UserReadService userReadService,
        final NoteWriteService noteWriteService
    ) {
        this.lockService = lockService;
        this.userReadService = userReadService;
        this.noteWriteService = noteWriteService;
    }

    public Long save(
        final DailygeUser dailygeUser,
        final NoteCreateCommand command
    ) {
        final Lock lock = lockService.getLock(dailygeUser.getUserId());
        try {
            if (!lock.tryLock(0, 4)) {
                throw CommonException.from(TOO_MANY_REQUEST);
            }
            final UserJpaEntity findUser = userReadService.findByNickname(command.getNickname());
            return noteWriteService.save(dailygeUser, command.toEntity(findUser.getId()));
        } catch (InterruptedException ex) {
            currentThread().interrupt();
            throw NoteTypeException.from(ex.getMessage(), NOTE_UN_RESOLVED_EXCEPTION);
        } finally {
            lockService.releaseLock(lock);
        }
    }
}
