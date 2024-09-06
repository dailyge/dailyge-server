package project.dailyge.app.core.coupon.application;

import project.dailyge.app.common.auth.DailygeUser;

public interface CouponWriteUseCase {
    void saveApply(final DailygeUser dailygeUser);
}
