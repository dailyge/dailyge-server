package project.dailyge.app.core.coupon.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.core.cache.coupon.CouponEventWriteUseCase;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RequestMapping("/api")
@PresentationLayer(value = "CouponBackgroundApi")
public class CouponBackgroundApi {

    private final CouponEventWriteUseCase couponEventWriteUseCase;

    @PostMapping(path = "/coupons/scheduling")
    public ResponseEntity<Void> processBulkRequests() {
        couponEventWriteUseCase.saveBulks();
        return ResponseEntity
            .status(CREATED)
            .build();
    }
}
