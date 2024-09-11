package project.dailyge.app.core.coupon.presentation;

import org.springframework.beans.factory.annotation.Value;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpStatus.CREATED;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import static project.dailyge.app.common.utils.CookieUtils.createResponseCookie;
import project.dailyge.app.core.coupon.application.CouponWriteUseCase;

@PresentationLayer
@RequestMapping("/api")
public class CouponCreateApi {

    private final String env;
    private final CouponWriteUseCase couponWriteUseCase;

    public CouponCreateApi(
        @Value("${env}") final String env,
        final CouponWriteUseCase couponWriteUseCase
    ) {
        this.env = env;
        this.couponWriteUseCase = couponWriteUseCase;
    }

    @PostMapping(path = "/coupons")
    public ResponseEntity<Void> createCouponApply(@LoginUser final DailygeUser dailygeUser) {
        couponWriteUseCase.saveApply(dailygeUser);
        final String responseCookie = createResponseCookie("isParticipated", "true", "/", 7L * 60L * 60L, true, env);
        return ResponseEntity
            .status(CREATED)
            .header(SET_COOKIE, responseCookie)
            .build();
    }
}
