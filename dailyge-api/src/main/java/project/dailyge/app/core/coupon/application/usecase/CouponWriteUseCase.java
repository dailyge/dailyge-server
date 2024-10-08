package project.dailyge.app.core.coupon.application.usecase;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.coupon.application.CouponWriteService;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipant;
import project.dailyge.app.core.coupon.persistence.CouponInMemoryRepository;
import project.dailyge.document.common.UuidGenerator;

@RequiredArgsConstructor
@ApplicationLayer(value = "CouponWriteService")
class CouponWriteUseCase implements CouponWriteService {

    private final CouponInMemoryRepository couponInMemoryRepository;

    @Override
    public void saveApply(final DailygeUser dailygeUser) {
        final CouponEventParticipant participant = new CouponEventParticipant(dailygeUser.getUserId(), UuidGenerator.createTimeStamp());
        couponInMemoryRepository.save(participant);
    }
}
