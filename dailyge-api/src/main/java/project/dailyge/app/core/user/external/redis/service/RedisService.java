package project.dailyge.app.core.user.external.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveRefreshToken(
        final Long userId,
        final String refreshToken
    ) {
        redisTemplate.opsForValue().set(userId.toString(), refreshToken);
    }

    private String getRefreshTokenKey(final Long userId) {
        return redisTemplate.opsForValue().get(userId.toString());
    }
}
