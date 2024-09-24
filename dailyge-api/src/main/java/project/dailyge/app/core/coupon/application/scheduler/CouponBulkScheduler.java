package project.dailyge.app.core.coupon.application.scheduler;

import static java.util.concurrent.TimeUnit.SECONDS;
import lombok.RequiredArgsConstructor;
import project.dailyge.app.codeandmessage.CommonCodeAndMessage;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.exception.CommonException;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

@RequiredArgsConstructor
@ApplicationLayer(value = "CouponBulkScheduler")
public class CouponBulkScheduler {

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> future = null;

    public synchronized void startFixedTask(
        final int period,
        final Runnable runnable
    ) {
        try {
            if (future == null || executorService.isShutdown()) {
                executorService = Executors.newSingleThreadScheduledExecutor();
                future = executorService.scheduleAtFixedRate(runnable, 0, period, SECONDS);
            }
        } catch (RejectedExecutionException exception) {
            throw CommonException.from(CommonCodeAndMessage.SERVICE_UNAVAILABLE);
        }
    }

    public synchronized void stop() {
        executorService.shutdown();
    }
}
