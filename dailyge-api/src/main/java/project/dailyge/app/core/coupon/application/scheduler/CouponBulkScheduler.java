package project.dailyge.app.core.coupon.application.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.dailyge.app.codeandmessage.CommonCodeAndMessage;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.core.cache.coupon.CouponCacheWriteUseCase;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
@RequiredArgsConstructor
public class CouponBulkScheduler {
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final CouponCacheWriteUseCase couponCacheWriteUseCase;
    private ScheduledFuture<?> future = null;

    public synchronized void startFixedTask(final int period) {
        try {
            if (future == null || executorService.isShutdown()) {
                future = executorService.scheduleAtFixedRate(couponCacheWriteUseCase::saveBulks, 0, period, SECONDS);
            }
        } catch (RejectedExecutionException exception) {
            throw CommonException.from(CommonCodeAndMessage.SERVICE_UNAVAILABLE);
        }
    }

    public synchronized void stop() {
        executorService.shutdown();
    }
}
