package project.dailyge.app.core.coupon.application;

import project.dailyge.app.core.common.auth.DailygeUser;

public interface CouponWriteService {
    void saveApply(DailygeUser dailygeUser);
}
