package project.dailyge.app.core.coupon.presentation.validator;

import org.springframework.stereotype.Component;
import project.dailyge.app.core.common.web.Cookies;

@Component
public class CouponClientValidator {

    private static final String TRUE = "true";

    public boolean validateParticipant(final String cookieValue) {
        return TRUE.equals(cookieValue);
    }

    public boolean isParticipated(final Cookies cookies) {
        return TRUE.equals(cookies.getValueByKey("isParticipated"));
    }
}
