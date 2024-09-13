package project.dailyge.app.core.coupon.application.service;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.coupon.application.CouponWriteUseCase;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipant;
import project.dailyge.app.core.coupon.persistence.CouponInMemoryRepository;
import project.dailyge.document.common.UuidGenerator;

@RequiredArgsConstructor
@ApplicationLayer(value = "CouponWriteService")
class CouponWriteService implements CouponWriteUseCase {

    private final CouponInMemoryRepository couponInMemoryRepository;

    @Override
    public void saveApply(final DailygeUser dailygeUser) {
        final CouponEventParticipant participant = new CouponEventParticipant(dailygeUser.getUserId(), UuidGenerator.createTimeStamp());
        couponInMemoryRepository.save(participant);
    }
}
