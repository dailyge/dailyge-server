package project.dailyge.app.core.coupon.exception;

import project.dailyge.app.common.exception.BusinessException;

import java.util.HashMap;
import java.util.Map;

import static project.dailyge.app.core.coupon.exception.CouponCodeAndMessage.COUPON_KEY_EXCEPTION;
import static project.dailyge.app.core.coupon.exception.CouponCodeAndMessage.COUPON_UN_RESOLVED_EXCEPTION;
import static project.dailyge.app.core.coupon.exception.CouponCodeAndMessage.DUPLICATED_WINNER_SELECTION;

public sealed class CouponTypeException extends BusinessException {

    private static final Map<CouponCodeAndMessage, CouponTypeException> exceptionMap = new HashMap<>();

    private CouponTypeException(final CouponCodeAndMessage codeAndMessage) {
        super(codeAndMessage);
    }

    static {
        exceptionMap.put(DUPLICATED_WINNER_SELECTION, new DuplicatedSelectionException(DUPLICATED_WINNER_SELECTION));
        exceptionMap.put(COUPON_UN_RESOLVED_EXCEPTION, new CouponUnResolvedException(COUPON_UN_RESOLVED_EXCEPTION));
        exceptionMap.put(COUPON_KEY_EXCEPTION, new CouponKeyException(COUPON_KEY_EXCEPTION));
    }

    public static CouponTypeException from(final CouponCodeAndMessage codeAndMessage) {
        final CouponTypeException couponTypeException = exceptionMap.get(codeAndMessage);
        if (couponTypeException == null) {
            return exceptionMap.get(COUPON_UN_RESOLVED_EXCEPTION);
        }
        return couponTypeException;
    }

    private static final class DuplicatedSelectionException extends CouponTypeException {
        public DuplicatedSelectionException(final CouponCodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class CouponKeyException extends CouponTypeException {
        public CouponKeyException(CouponCodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }

    private static final class CouponUnResolvedException extends CouponTypeException {
        public CouponUnResolvedException(final CouponCodeAndMessage codeAndMessage) {
            super(codeAndMessage);
        }
    }
}
