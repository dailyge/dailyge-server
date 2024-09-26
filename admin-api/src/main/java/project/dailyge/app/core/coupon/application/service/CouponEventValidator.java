package project.dailyge.app.core.coupon.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.dailyge.app.core.coupon.exception.CouponTypeException;
import project.dailyge.core.cache.coupon.CouponCacheReadRepository;

import static project.dailyge.app.core.coupon.exception.CouponCodeAndMessage.DUPLICATED_WINNER_SELECTION;

@Component
@RequiredArgsConstructor
public class CouponEventValidator {

    private final CouponCacheReadRepository couponCacheReadRepository;

    public void validateEventRun(final Long eventId) {
        if (couponCacheReadRepository.isExecuted(eventId)) {
            throw CouponTypeException.from(DUPLICATED_WINNER_SELECTION);
        }
    }
}
