package project.dailyge.app.core.common.external.redis;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.lock.Lock;
import project.dailyge.lock.LockService;

@RequiredArgsConstructor
@ApplicationLayer(value = "LockUseCase")
public class LockUseCase implements LockService {

    private final RedissonClient redissonClient;

    @Override
    public Lock getLock(final Long userId) {
        return RedisUtils.getLock(redissonClient, userId);
    }

    @Override
    public void releaseLock(final Lock lock) {
        RedisUtils.releaseLock(lock);
    }
}
