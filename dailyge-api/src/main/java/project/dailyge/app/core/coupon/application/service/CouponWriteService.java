package project.dailyge.app.core.coupon.application.service;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.coupon.application.CouponWriteUseCase;
import project.dailyge.entity.coupon.CouponEventParticipant;
import project.dailyge.entity.coupon.CouponEventParticipantRepository;

@ApplicationLayer
@RequiredArgsConstructor
class CouponWriteService implements CouponWriteUseCase {
	private final CouponEventParticipantRepository couponEventParticipantRepository;

	@Override
	public void saveApply(
		final DailygeUser dailygeUser,
		final LocalDateTime dateTime
	) {
		couponEventParticipantRepository.save(new CouponEventParticipant(dailygeUser.getUserId(), dateTime));
	}
}
