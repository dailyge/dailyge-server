package project.dailyge.app.core.coupon.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.core.coupon.application.scheduler.CouponBulkScheduler;
import project.dailyge.app.core.coupon.presentation.request.ScheduleRateRequest;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@PresentationLayer
@RequestMapping("/api")
@RequiredArgsConstructor
public class CouponSchedulerApi {
    private final CouponBulkScheduler couponBulkScheduler;

    @PostMapping(path = "/coupons/scheduling")
    public ResponseEntity<Void> startScheduler(@RequestBody final ScheduleRateRequest schedulerRateRequest) {
        couponBulkScheduler.startFixedTask(schedulerRateRequest.period());
        return ResponseEntity
            .status(CREATED)
            .build();
    }

    @DeleteMapping(path = "/coupons/scheduling")
    public ResponseEntity<Void> stopScheduler() {
        couponBulkScheduler.stop();
        return ResponseEntity
            .status(NO_CONTENT)
            .build();
    }
}
