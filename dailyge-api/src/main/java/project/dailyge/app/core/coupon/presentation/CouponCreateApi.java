package project.dailyge.app.core.coupon.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.core.coupon.application.CouponWriteUseCase;

import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpStatus.CREATED;
import static project.dailyge.app.common.utils.CookieUtils.createResponseCookie;

@PresentationLayer
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponCreateApi {

    private final CouponWriteUseCase couponWriteUseCase;

    @PostMapping(path = "/coupons")
    public ResponseEntity<Void> createCouponApply(@LoginUser final DailygeUser dailygeUser) {
        couponWriteUseCase.saveApply(dailygeUser);
        final String responseCookie = createResponseCookie("isParticipated", "true", "/", 7 * 60 * 60, true);
        return ResponseEntity
            .status(CREATED)
            .header(SET_COOKIE, responseCookie)
            .build();
    }
}
