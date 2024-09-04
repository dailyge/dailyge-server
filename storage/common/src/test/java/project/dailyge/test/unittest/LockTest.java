package project.dailyge.test.unittest;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.redisson.api.RLock;
import project.dailyge.lock.Lock;

@DisplayName("[UnitTest] Lock 단위 테스트")
class LockTest {

    private RLock rLock;

    @BeforeEach
    void setUp() {
        rLock = mock(RLock.class);
    }

    @Test
    @DisplayName("정적 메서드를 통해 Lock을 생성할 수 있다.")
    void whenCreateLockWithStaticMethodThenObjectShouldBeNotNull() {
        rLock = mock(RLock.class);
        assertNotNull(Lock.createLock(rLock));
    }

    @Test
    @DisplayName("락이 잡혀 있다면 true를 반환한다.")
    void whenAcquireLockThenReturnTrue() throws InterruptedException {
        when(rLock.tryLock(anyLong(), anyLong(), eq(SECONDS))).thenReturn(true);

        final Lock lock = Lock.createLock(rLock);

        assertTrue(lock.tryLock(0, 4));
    }

    @Test
    @DisplayName("락을 잡지 못했을 때 false를 반환한다.")
    void whenLockIsNotAcquiredThenReturnFalse() throws InterruptedException {
        when(rLock.tryLock(anyLong(), anyLong(), eq(SECONDS))).thenReturn(false);

        final Lock lock = Lock.createLock(rLock);

        assertFalse(lock.tryLock(0, 4));
    }

    @Test
    @DisplayName("Lock이 현재 스레드에 의해 확보된 경우 true를 반환한다.")
    void whenLockIsHeldByCurrentThreadThenReturnTrue() {
        when(rLock.isHeldByCurrentThread()).thenReturn(true);

        final Lock lock = Lock.createLock(rLock);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    @DisplayName("Lock이 현재 스레드에 의해 확보되지 않은 경우 false를 반환한다.")
    void whenLockIsNotHeldByCurrentThreadThenReturnFalse() {
        when(rLock.isHeldByCurrentThread()).thenReturn(false);

        final Lock lock = Lock.createLock(rLock);
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    @DisplayName("Unlock 메서드를 호출하면 락이 해제된다.")
    void whenUnlockThenReleaseLock() {
        final Lock lock = Lock.createLock(rLock);
        lock.unlock();

        verify(rLock).unlock();
    }
}
