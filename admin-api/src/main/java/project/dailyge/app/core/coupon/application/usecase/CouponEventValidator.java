package project.dailyge.app.core.coupon.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.dailyge.app.core.coupon.exception.CouponTypeException;
import project.dailyge.core.cache.coupon.CouponEventReadRepository;

import static project.dailyge.app.core.coupon.exception.CouponCodeAndMessage.DUPLICATED_WINNER_SELECTION;

@Component
@RequiredArgsConstructor
public class CouponEventValidator {

    private final CouponEventReadRepository couponEventReadRepository;

    public void validateEventRun(final Long eventId) {
        if (couponEventReadRepository.isExecuted(eventId)) {
            throw CouponTypeException.from(DUPLICATED_WINNER_SELECTION);
        }
    }
}
