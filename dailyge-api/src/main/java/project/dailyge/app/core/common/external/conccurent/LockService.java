package project.dailyge.app.core.common.external.conccurent;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import project.dailyge.app.core.common.external.redis.RedisUtils;
import project.dailyge.lock.Lock;
import project.dailyge.lock.LockUseCase;

@Service
@RequiredArgsConstructor
public class LockService implements LockUseCase {

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
