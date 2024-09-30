package project.dailyge.app.core.coupon.application.facade;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.FacadeLayer;
import project.dailyge.app.core.coupon.application.CouponUseCase;
import project.dailyge.app.core.coupon.application.FreeCouponWriteUseCase;

import java.util.List;

@RequiredArgsConstructor
@FacadeLayer(value = "CouponFacade")
public class CouponFacade {

    private final CouponUseCase couponUseCase;
    private final FreeCouponWriteUseCase freeCouponWriteUseCase;

    public void pickWinners(
        final int winnerCount,
        final Long eventId
    ) {
        final List<Long> userIds = couponUseCase.pickWinners(winnerCount, eventId);
        freeCouponWriteUseCase.saveAll(userIds, eventId);
    }
}
