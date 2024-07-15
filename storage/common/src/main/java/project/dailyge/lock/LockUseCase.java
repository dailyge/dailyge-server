package project.dailyge.lock;

public interface LockUseCase {
    Lock getLock(Long userId);

    void releaseLock(Lock lock);
}
