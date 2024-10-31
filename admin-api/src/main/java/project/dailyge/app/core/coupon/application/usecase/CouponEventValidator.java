package project.dailyge.app.core.coupon.application.usecase;

import org.springframework.stereotype.Component;
import static project.dailyge.app.core.coupon.exception.CouponCodeAndMessage.DUPLICATED_WINNER_SELECTION;
import project.dailyge.app.core.coupon.exception.CouponTypeException;
import project.dailyge.core.cache.coupon.CouponEventReadRepository;

@Component
public class CouponEventValidator {

    private final CouponEventReadRepository couponEventReadRepository;

    public CouponEventValidator(final CouponEventReadRepository couponEventReadRepository) {
        this.couponEventReadRepository = couponEventReadRepository;
    }

    public void validateEventRun(final Long eventId) {
        if (couponEventReadRepository.isExecuted(eventId)) {
            throw CouponTypeException.from(DUPLICATED_WINNER_SELECTION);
        }
    }
}
