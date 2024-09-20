package project.dailyge.app.core.event.facade;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.FacadeLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.coupon.application.CouponWriteUseCase;
import project.dailyge.app.core.event.application.service.EventValidator;
import project.dailyge.app.core.event.persistence.EventCache;
import project.dailyge.app.core.event.persistence.LocalEventCache;

import static java.time.LocalDateTime.now;

@RequiredArgsConstructor
@FacadeLayer(value = "EventFacade")
public class EventFacade {

    private final LocalEventCache localEventCache;
    private final CouponWriteUseCase couponWriteUseCase;
    private final EventValidator eventValidator;

    @PostConstruct
    public void init() {
        localEventCache.save(new EventCache(1L, now(), now().plusMonths(1), 0L));
    }

    public void participateEvent(
        final DailygeUser dailygeUser,
        final Long eventId
    ) {
        eventValidator.validate(eventId);
        couponWriteUseCase.saveApply(dailygeUser);
    }
}
