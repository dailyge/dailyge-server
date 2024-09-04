package project.dailyge.lock;

import static java.util.concurrent.TimeUnit.SECONDS;
import org.redisson.api.RLock;

public final class Lock {

    private final RLock rLock;

    private Lock() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    private Lock(final RLock rLock) {
        this.rLock = rLock;
    }

    public static Lock createLock(final RLock rLock) {
        return new Lock(rLock);
    }

    public boolean tryLock(
        final long waitTime,
        final long leaseTime
    ) throws InterruptedException {
        return rLock.tryLock(waitTime, leaseTime, SECONDS);
    }

    public boolean isHeldByCurrentThread() {
        return rLock.isHeldByCurrentThread();
    }

    public void unlock() {
        rLock.unlock();
    }
}
