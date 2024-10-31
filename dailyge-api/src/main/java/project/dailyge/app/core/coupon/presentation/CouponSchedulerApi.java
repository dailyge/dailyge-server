package project.dailyge.app.core.coupon.presentation;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.CREATED;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.NO_CONTENT;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.coupon.application.scheduler.CouponBulkScheduler;
import project.dailyge.app.core.coupon.presentation.request.ScheduleRateRequest;
import project.dailyge.core.cache.coupon.CouponEventWriteService;

@PresentationLayer(value = "CouponSchedulerApi")
@RequestMapping(path = "/api/coupons")
public class CouponSchedulerApi {

    private final CouponBulkScheduler couponBulkScheduler;
    private final CouponEventWriteService couponEventWriteService;

    public CouponSchedulerApi(
        final CouponBulkScheduler couponBulkScheduler,
        final CouponEventWriteService couponEventWriteService
    ) {
        this.couponBulkScheduler = couponBulkScheduler;
        this.couponEventWriteService = couponEventWriteService;
    }

    @PostMapping(path = "/scheduling")
    public ApiResponse<Void> startScheduler(@RequestBody final ScheduleRateRequest request) {
        couponBulkScheduler.startFixedTask(request.period(), couponEventWriteService::saveBulks);
        return ApiResponse.from(CREATED);
    }

    @DeleteMapping(path = "/scheduling")
    public ApiResponse<Void> stopScheduler() {
        couponBulkScheduler.stop();
        return ApiResponse.from(NO_CONTENT);
    }
}
