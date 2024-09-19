package project.dailyge.app.core.coupon.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.coupon.application.scheduler.CouponBulkScheduler;
import project.dailyge.app.core.coupon.presentation.request.ScheduleRateRequest;
import project.dailyge.core.cache.coupon.CouponCacheWriteUseCase;

import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.CREATED;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.NO_CONTENT;

@PresentationLayer
@RequestMapping("/api")
@RequiredArgsConstructor
public class CouponSchedulerApi {
    private final CouponBulkScheduler couponBulkScheduler;
    private final CouponCacheWriteUseCase couponCacheWriteUseCase;

    @PostMapping(path = "/coupons/scheduling")
    public ApiResponse<Void> startScheduler(@RequestBody final ScheduleRateRequest schedulerRateRequest) {
        couponBulkScheduler.startFixedTask(schedulerRateRequest.period(), couponCacheWriteUseCase::saveBulks);
        return ApiResponse.from(CREATED);
    }

    @DeleteMapping(path = "/coupons/scheduling")
    public ApiResponse<Void> stopScheduler() {
        couponBulkScheduler.stop();
        return ApiResponse.from(NO_CONTENT);
    }
}
