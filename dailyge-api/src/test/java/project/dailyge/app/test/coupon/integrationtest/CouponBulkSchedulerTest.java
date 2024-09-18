package project.dailyge.app.test.coupon.integrationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.coupon.application.scheduler.CouponBulkScheduler;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipant;
import project.dailyge.app.core.coupon.persistence.CouponInMemoryRepository;
import project.dailyge.core.cache.coupon.CouponCacheReadUseCase;
import project.dailyge.core.cache.coupon.CouponCacheWriteUseCase;

import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[통합 테스트] CouponBulkScheduler 동작 테스트")
public class CouponBulkSchedulerTest extends DatabaseTestBase {

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
}
