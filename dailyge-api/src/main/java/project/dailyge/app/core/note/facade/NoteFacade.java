package project.dailyge.app.core.note.facade;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.TOO_MANY_REQUEST;
import project.dailyge.app.common.annotation.FacadeLayer;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.note.application.NoteWriteService;
import project.dailyge.app.core.note.application.command.NoteCreateCommand;
import project.dailyge.app.core.user.application.UserReadService;
import project.dailyge.common.ratelimiter.RateLimiterReadService;
import project.dailyge.common.ratelimiter.RateLimiterWriteService;
import project.dailyge.entity.user.UserJpaEntity;
import project.dailyge.lock.Lock;
import project.dailyge.lock.LockService;

@FacadeLayer(value = "NoteFacade")
public class NoteFacade {

    private final LockService lockService;
    private final RateLimiterReadService rateLimiterReadService;
    private final RateLimiterWriteService rateLimiterWriteService;
    private final UserReadService userReadService;
    private final NoteWriteService noteWriteService;

    public NoteFacade(
        final LockService lockService,
        final RateLimiterReadService rateLimiterReadService,
        final RateLimiterWriteService rateLimiterWriteService,
        final UserReadService userReadService,
        final NoteWriteService noteWriteService
    ) {
        this.lockService = lockService;
        this.rateLimiterReadService = rateLimiterReadService;
        this.rateLimiterWriteService = rateLimiterWriteService;
        this.userReadService = userReadService;
        this.noteWriteService = noteWriteService;
    }

    public Long save(
        final DailygeUser dailygeUser,
        final NoteCreateCommand command,
        final long time
    ) {
        try {
            final Lock lock = lockService.getLock(dailygeUser.getUserId());
            if (!lock.tryLock(0, 4)) {
                throw CommonException.from(TOO_MANY_REQUEST);
            }
            final boolean hasHistory = rateLimiterReadService.findNoteHistoryByUserId(dailygeUser.getUserId());
            if (!hasHistory) {
                final UserJpaEntity findUser = userReadService.findByNickname(command.getNickname());
                rateLimiterWriteService.save(dailygeUser.getUserId(), time);
                return noteWriteService.save(dailygeUser, command.toEntity(findUser.getId()));
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        throw CommonException.from(TOO_MANY_REQUEST);
    }
}
