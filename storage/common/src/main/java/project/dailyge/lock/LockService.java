package project.dailyge.lock;

public interface LockService {
    Lock getLock(Long userId);

    void releaseLock(Lock lock);
}
