package project.dailyge.app.core.coupon.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.core.cache.coupon.CouponCacheWriteUseCase;

import static org.springframework.http.HttpStatus.CREATED;

@PresentationLayer
@RequiredArgsConstructor
@RequestMapping("/api")
public class CouponBackgroundApi {

    private final CouponCacheWriteUseCase couponCacheWriteUseCase;

    @PostMapping(path = "/coupons/scheduling")
    public ResponseEntity<Void> processBulkRequests() {
        couponCacheWriteUseCase.saveBulks();
        return ResponseEntity
            .status(CREATED)
            .build();
    }
}
