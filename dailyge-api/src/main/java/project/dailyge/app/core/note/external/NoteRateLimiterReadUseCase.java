package project.dailyge.app.core.note.external;

import org.springframework.data.redis.core.StringRedisTemplate;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.DATA_ACCESS_EXCEPTION;
import project.dailyge.app.common.annotation.ApplicationLayer;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.common.ratelimiter.RateLimiterReadService;

@ApplicationLayer(value = "NoteRateLimiterReadUseCase")
class NoteRateLimiterReadUseCase implements RateLimiterReadService {

    private final StringRedisTemplate redisTemplate;

    public NoteRateLimiterReadUseCase(final StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean findNoteHistoryByUserId(final long userId) {
        try {
            final Object findHistory = redisTemplate.opsForValue().get(getRateLimiterKey(userId));
            if (findHistory != null) {
                return true;
            }
        } catch (Exception ex) {
            throw CommonException.from(ex.getMessage(), DATA_ACCESS_EXCEPTION);
        }
        return false;
    }

    private String getRateLimiterKey(final Long userId) {
        return String.format("note:user:%d", userId);
    }
}
