package project.dailyge.app.test.coupon.integrationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.coupon.application.scheduler.CouponBulkScheduler;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipant;
import project.dailyge.app.core.coupon.persistence.CouponInMemoryRepository;
import project.dailyge.core.cache.coupon.CouponCacheReadUseCase;
import project.dailyge.core.cache.coupon.CouponCacheWriteUseCase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[통합 테스트] CouponBulkScheduler 동작 테스트")
public class CouponBulkSchedulerTest extends DatabaseTestBase {

    private static final Logger log = LoggerFactory.getLogger(CouponBulkSchedulerTest.class);

    @Autowired
    private CouponInMemoryRepository couponInMemoryRepository;

    @Autowired
    private CouponBulkScheduler couponBulkScheduler;

    @Autowired
    private CouponCacheReadUseCase couponCacheReadUseCase;

    @Autowired
    private CouponCacheWriteUseCase couponCacheWriteUseCase;

    @BeforeEach
    void setUp() {
        LongStream.range(1L, 10000L)
            .forEach(number -> couponInMemoryRepository.save(new CouponEventParticipant(number, number)));
    }

    @Test
    @Disabled
    @DisplayName("스케줄러가 시작하면 폴링 작업을 한다.")
    void whenStartSchedulerThenPollingRuns() throws InterruptedException {
        couponBulkScheduler.startFixedTask(5, couponCacheWriteUseCase::saveBulks);
        Thread.sleep(30000);
        couponBulkScheduler.stop();
        assertTrue(couponCacheReadUseCase.existsByUserId(1L));
    }

    @Test
    @Disabled
    @DisplayName("멀티스레드 환경에서 스케줄러는 한번만 시작된다.")
    void whenAccessByMultiThreadThenSchedulerStartOnce() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(100);
        final ExecutorService service = Executors.newFixedThreadPool(32);
        for (int index = 1; index <= 100; index++) {
            try {
                service.execute(() -> {
                    couponBulkScheduler.startFixedTask(5, this::execute);
                });
            } finally {
                countDownLatch.countDown();
            }
        }
        Thread.sleep(30000);
        //log를 통해 스케줄러가 한번만 시작됨을 알 수 있습니다.
    }

    private void execute() {
        log.info("test!");
    }
}
