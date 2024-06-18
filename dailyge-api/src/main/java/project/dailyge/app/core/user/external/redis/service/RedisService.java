package project.dailyge.app.core.user.external.redis.service;

import io.lettuce.core.RedisConnectionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveRefreshToken(
        final Long userId,
        final String refreshToken
    ) {
        try {
            redisTemplate.opsForValue().set(userId.toString(), refreshToken);
        } catch (RedisConnectionException e) {
            log.error("Redis 연결 문제 발생: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("알 수 없는 Redis 에러 발생: {}", e.getMessage());
            throw e;
        }
    }

    private String getRefreshTokenKey(final Long userId) {
        try {
            return redisTemplate.opsForValue().get(userId.toString());
        } catch (RedisConnectionException e) {
            log.error("Redis 연결 문제 발생: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("알 수 없는 Redis 에러 발생: {}", e.getMessage());
            throw e;
        }
    }
}
