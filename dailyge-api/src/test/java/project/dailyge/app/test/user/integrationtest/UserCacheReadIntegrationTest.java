package project.dailyge.app.test.user.integrationtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.user.persistence.UserCacheReadDao;
import project.dailyge.app.core.user.persistence.UserCacheWriteDao;
import static project.dailyge.common.configuration.CompressionHelper.compressAsByteArrayWithZstd;
import project.dailyge.core.cache.user.UserCache;

@DisplayName("[IntegrationTest] 사용자 캐시 조회 통합 테스트(with Blacklist)")
class UserCacheReadIntegrationTest extends DatabaseTestBase {

    @Autowired
    private UserCacheWriteDao userCacheWriteDao;

    @Autowired
    private UserCacheReadDao userCacheReadDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate<String, byte[]> redisTemplate;

    @Test
    @DisplayName("블랙 리스트로 등록 돼 있다면, 캐시가 존재하더라도 null을 반환한다.")
    void whenRegisteredAsBlackListThenEvenIfUserCacheExistsThenResultShouldBeNull() {
        final UserCache userCache = new UserCache(
            1L,
            "dailyge",
            "dailyge@gmail.com",
            "",
            "NORMAL"
        );
        userCacheWriteDao.save(userCache);
        final String blackListKey = String.format("user:blacklist:%d", userCache.getId());
        redisTemplate.opsForValue().set(blackListKey, compressAsByteArrayWithZstd(userCache, objectMapper));
        assertNull(userCacheReadDao.findById(userCache.getId()));
    }

    @Test
    @DisplayName("(블랙 리스트에 등록되지 않은) 일반 사용자라면, 캐시가 존재할 때, 캐시가 반환된다.")
    void whenNormalUserAndCacheExistsThenResultShouldNotBeNull() {
        final UserCache userCache = new UserCache(
            1L,
            "dailyge",
            "dailyge@gmail.com",
            "",
            "NORMAL"
        );
        userCacheWriteDao.save(userCache);
        assertAll(
            () -> assertNotNull(userCacheReadDao.findById(userCache.getId())),
            () -> assertEquals(1L, userCache.getId()),
            () -> assertEquals("dailyge", userCache.getNickname()),
            () -> assertEquals("dailyge@gmail.com", userCache.getEmail()),
            () -> assertEquals("", userCache.getProfileImageUrl())
        );
    }

    @Test
    @DisplayName("캐시가 존재하지 않으면, null이 반환된다.")
    void whenNormalUserAndCacheNotExistsThenResultShouldBeNull() {
        assertNull(userCacheReadDao.findById(Long.MAX_VALUE));
    }
}
