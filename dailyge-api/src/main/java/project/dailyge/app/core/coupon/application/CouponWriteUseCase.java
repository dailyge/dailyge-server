package project.dailyge.app.core.coupon.application;

import java.time.LocalDateTime;

import project.dailyge.app.common.auth.DailygeUser;

public interface CouponWriteUseCase {
	void saveApply(DailygeUser dailygeUser, LocalDateTime localDateTime);
}
