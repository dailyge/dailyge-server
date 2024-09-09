package project.dailyge.app.test.coupon.unittest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import project.dailyge.app.core.coupon.persistence.CouponEventMemoryQueue;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipant;
import project.dailyge.app.core.coupon.persistence.CouponInMemoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@DisplayName("[UnitTest] 쿠폰발급 인메모리큐 단위 테스트")
class CouponEventParticipantInMemoryQueueUnitTest {

    private BlockingQueue<CouponEventParticipant> queue;
    private CouponInMemoryRepository repository;

    @BeforeEach
    void setUp() {
        queue = new LinkedBlockingQueue<>();
        repository = new CouponEventMemoryQueue(queue);
    }

    @ParameterizedTest
    @DisplayName("요소를 넣은 횟수에 따라 인메모리큐 크기가 증가한다.")
    @ValueSource(ints = {3, 4, 5, 6, 7, 8})
    void whenCreateCouponParticipationThenIncreaseQueueSize(final int count) {
        createCouponParticipantsBySize(count);
        Assertions.assertEquals(count, repository.count());
    }


    @Test
    @DisplayName("인메모리큐에 요소가 없으면 popAll 반환 리스트의 크기는 0이다.")
    void whenQueueIsEmptyThenPopAllResultListSizeZero() {
        repository.deleteAll();
        final List<CouponEventParticipant> participants = repository.popAll();
        Assertions.assertEquals(0, participants.size());
    }

    @ParameterizedTest
    @DisplayName("인메모리에 요소가 있으면 popAll 반환 리스트는 전체 요소들을 순서대로 반환한다.")
    @ValueSource(ints = {3, 4, 5, 6, 7, 8})
    void whenQueueIsNotEmptyPopAllResultReturnElementsByOrder(final int count) {
        final List<CouponEventParticipant> expectedResult = createCouponParticipantsBySize(count);
        final List<CouponEventParticipant> actualResult = repository.popAll();
        Assertions.assertEquals(expectedResult, actualResult);
    }

    private List<CouponEventParticipant> createCouponParticipantsBySize(int count) {
        final List<CouponEventParticipant> participants = new ArrayList<>();
        for (int id = 1; id <= count; id++) {
            long userId = id;
            CouponEventParticipant participant = new CouponEventParticipant(userId, System.currentTimeMillis());
            participants.add(participant);
            repository.save(participant);
        }
        return participants;
    }
}
