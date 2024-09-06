package project.dailyge.app.core.coupon.presentation.validator;

import org.springframework.stereotype.Component;
import project.dailyge.app.core.common.web.Cookies;

@Component
public class CouponClientValidator {
    public boolean validateParticipant(final String cookieValue) {
        return "true".equals(cookieValue);
    }

    public boolean isParticipated(final Cookies cookies) {
        return "true".equals(cookies.getValueByKey("isParticipated"));
    }
}
