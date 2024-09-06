package project.dailyge.app.test.coupon.unittest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.util.ReflectionTestUtils;
import project.dailyge.app.core.coupon.configuration.BlockingQueueConfig;
import project.dailyge.app.core.coupon.persistence.CouponEventParticipant;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@DisplayName("[UnitTest] 인메모리큐 설정 단위 테스트")
class BlockingQueueConfigUnitTest {
    private BlockingQueueConfig config;

    @BeforeEach
    void setUp() {
        config = new BlockingQueueConfig();
    }

    @ParameterizedTest
    @MethodSource("queueTypeAndClassProvider")
    @DisplayName("큐 설정값에 따라 지정된 BlockingQueue 구현체를 반환한다.")
    void whenSetValidValueThenReturnCertainBlockingQueueInstance(
        final String queueType,
        final Class<? extends BlockingQueue> expectedQueueClass
    ) {
        ReflectionTestUtils.setField(config, "queueType", queueType);
        ReflectionTestUtils.setField(config, "queueCapacity", 1000);
        BlockingQueue<CouponEventParticipant> queue = config.couponEventParticipantQueue();
        assertInstanceOf(expectedQueueClass, queue);
        if (queue instanceof ArrayBlockingQueue) {
            assertEquals(1000, queue.remainingCapacity() + queue.size());
        }
    }

    private static Stream<Arguments> queueTypeAndClassProvider() {
        return Stream.of(
            Arguments.of("linked", LinkedBlockingQueue.class),
            Arguments.of("array", ArrayBlockingQueue.class),
            Arguments.of("priority", PriorityBlockingQueue.class),
            Arguments.of("invalid", LinkedBlockingQueue.class)
        );
    }
}
