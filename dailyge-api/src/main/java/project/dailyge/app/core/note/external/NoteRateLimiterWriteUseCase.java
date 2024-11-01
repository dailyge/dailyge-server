package project.dailyge.app.core.note.external;

import static java.util.concurrent.TimeUnit.SECONDS;
import org.springframework.data.redis.core.StringRedisTemplate;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.DATA_ACCESS_EXCEPTION;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.common.ratelimiter.RateLimiterWriteService;

@ApplicationLayer(value = "NoteRateLimiterWriteUseCase")
class NoteRateLimiterWriteUseCase implements RateLimiterWriteService {

    private final StringRedisTemplate redisTemplate;

    public NoteRateLimiterWriteUseCase(final StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(
        final long userId,
        final long time
    ) {
        try {
            redisTemplate.opsForValue().set(getRateLimiterKey(userId), String.valueOf(userId), time, SECONDS);
        } catch (Exception ex) {
            throw CommonException.from(ex.getMessage(), DATA_ACCESS_EXCEPTION);
        }
    }

    @Override
    public void deleteById(final long userId) {
        try {
            redisTemplate.opsForValue().getAndDelete(getRateLimiterKey(userId));
        } catch (Exception ex) {
            throw CommonException.from(ex.getMessage(), DATA_ACCESS_EXCEPTION);
        }
    }

    private String getRateLimiterKey(final Long userId) {
        return String.format("note:user:%d", userId);
    }
}
