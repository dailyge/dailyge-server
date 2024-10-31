package project.dailyge.app.core.coupon.application.usecase;

import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.coupon.application.CouponWriteService;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipant;
import project.dailyge.app.core.coupon.persistence.CouponInMemoryRepository;
import project.dailyge.document.common.UuidGenerator;

@ApplicationLayer(value = "CouponWriteService")
class CouponWriteUseCase implements CouponWriteService {

    private final CouponInMemoryRepository couponInMemoryRepository;

    public CouponWriteUseCase(final CouponInMemoryRepository couponInMemoryRepository) {
        this.couponInMemoryRepository = couponInMemoryRepository;
    }

    @Override
    public void saveApply(final DailygeUser dailygeUser) {
        final CouponEventParticipant participant = new CouponEventParticipant(dailygeUser.getUserId(), UuidGenerator.createTimeStamp());
        couponInMemoryRepository.save(participant);
    }
}
