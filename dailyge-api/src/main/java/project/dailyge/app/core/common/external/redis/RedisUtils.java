package project.dailyge.app.core.common.external.redis;

import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_REQUEST;

import project.dailyge.app.common.exception.CommonException;
import project.dailyge.lock.Lock;

public final class RedisUtils {

    private RedisUtils() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static Lock getLock(
        final RedissonClient redissonClient,
        final Long userId
    ) {
        try {
            return Lock.createLock(redissonClient.getLock(getLockKey(userId)));
        } catch (IllegalArgumentException ex) {
            throw CommonException.from(ex.getMessage(), BAD_REQUEST);
        } catch (RedisException ex) {
            throw CommonException.from(ex.getMessage(), BAD_GATEWAY);
        }
    }

    public static void releaseLock(final Lock lock) {
        try {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        } catch (IllegalArgumentException ex) {
            throw CommonException.from(ex.getMessage(), BAD_REQUEST);
        } catch (RedisException ex) {
            throw CommonException.from(ex.getMessage(), BAD_GATEWAY);
        }
    }

    private static String getLockKey(final Long userId) {
        return String.format("user::lock::%s", userId);
    }
}
