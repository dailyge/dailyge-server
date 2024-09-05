package project.dailyge.app.core.coupon.application.service;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.coupon.application.CouponWriteUseCase;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipant;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipantRepository;
import project.dailyge.document.common.UuidGenerator;

@ApplicationLayer
@RequiredArgsConstructor
class CouponWriteService implements CouponWriteUseCase {

    private final CouponEventParticipantRepository couponEventParticipantRepository;

    @Override
    public void saveApply(
        final DailygeUser dailygeUser
    ) {
        couponEventParticipantRepository.save(new CouponEventParticipant(dailygeUser.getUserId(), UuidGenerator.createTimeStamp()));
    }
}
