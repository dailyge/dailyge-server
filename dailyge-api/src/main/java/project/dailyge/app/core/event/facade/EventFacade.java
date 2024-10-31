package project.dailyge.app.core.event.facade;

import jakarta.annotation.PostConstruct;
import static java.time.LocalDateTime.now;
import project.dailyge.app.common.annotation.FacadeLayer;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.coupon.application.CouponWriteService;
import project.dailyge.app.core.event.application.usecase.EventValidator;
import project.dailyge.app.core.event.persistence.EventCache;
import project.dailyge.app.core.event.persistence.LocalEventCache;

@FacadeLayer(value = "EventFacade")
public class EventFacade {

    private final LocalEventCache localEventCache;
    private final CouponWriteService couponWriteService;
    private final EventValidator eventValidator;

    public EventFacade(
        final LocalEventCache localEventCache,
        final CouponWriteService couponWriteService,
        final EventValidator eventValidator
    ) {
        this.localEventCache = localEventCache;
        this.couponWriteService = couponWriteService;
        this.eventValidator = eventValidator;
    }

    @PostConstruct
    public void init() {
        localEventCache.save(new EventCache(1L, now(), now().plusMonths(1), 0L));
    }

    public void participateEvent(
        final DailygeUser dailygeUser,
        final Long eventId
    ) {
        eventValidator.validate(eventId);
        couponWriteService.saveApply(dailygeUser);
    }
}
