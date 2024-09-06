package project.dailyge.app.test.coupon.unittest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipant;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipantInMemoryQueue;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipantRepository;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@DisplayName("[UnitTest] 쿠폰발급 인메모리큐 단위 테스트")
class CouponEventParticipantInMemoryQueueUnitTest {
    @ParameterizedTest
    @DisplayName("요소를 넣은 횟수에 따라 인메모리큐 크기가 증가한다.")
    @ValueSource(ints = {3, 4, 5, 6, 7, 8})
    void whenCreateCouponParticipationThenIncreaseQueueSize(final int count) {
        BlockingQueue<CouponEventParticipant> queue = new LinkedBlockingQueue<>();
        CouponEventParticipantRepository repository = new CouponEventParticipantInMemoryQueue(queue);
        for (int id = 1; id <= count; id++) {
            long userId = id;
            CouponEventParticipant participant = new CouponEventParticipant(userId, System.currentTimeMillis());
            repository.save(participant);
        }
        Assertions.assertEquals(count, repository.count());
    }
}
