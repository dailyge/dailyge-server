package project.dailyge.app.test.user.integrationtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import static java.lang.System.nanoTime;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.user.persistence.UserCacheReadDao;
import project.dailyge.app.core.user.persistence.UserCacheWriteDao;
import static project.dailyge.common.configuration.CompressionHelper.compressAsByteArrayWithZstd;
import static project.dailyge.common.configuration.CompressionHelper.decompressAsObjWithZstd;
import project.dailyge.core.cache.user.UserCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

@DisplayName("[IntegrationTest] 사용자 캐시 조회 통합 테스트(with Blacklist)")
class UserCacheReadIntegrationTest extends DatabaseTestBase {

    private static final Logger log = LoggerFactory.getLogger(UserCacheReadIntegrationTest.class);

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

    @Test
    @Disabled
    @DisplayName("LuaScript를 사용하면 기존 방식보다 시간이 짧게 걸린다.")
    void whenUseLuaScriptThenExecutionTimeShouldBeShorten() throws Exception {
        final int totalCount = 50_000;
        final Random random = new Random(totalCount + 1);
        final ExecutorService executorService = newFixedThreadPool(16);
        final CountDownLatch latch = new CountDownLatch(totalCount);

        initCacheData(random);

        final long startTime = nanoTime();
        for (int index = 1; index <= totalCount; index++) {
            final long userId = random.nextLong();
            executorService.submit(() -> {
                try {
                    userCacheReadDao.findById(userId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        final long endTime = nanoTime();
        final long durationInMillis = NANOSECONDS.toMillis(endTime - startTime);
        log.info("LuaScript duration: {} ms", durationInMillis);
        executorService.shutdown();
    }

    @Test
    @Disabled
    @DisplayName("기존 방식을 사용하면 LuaScript를 사용할 때 보다 시간이 오래 걸린다.")
    void whenOriginSearchThenExecutionTimeShouldBeLongerThenLuaScript() throws InterruptedException {
        final int totalCount = 50_000;
        final Random random = new Random(totalCount + 1);
        final ExecutorService executorService = newFixedThreadPool(16);
        final CountDownLatch latch = new CountDownLatch(totalCount);

        initCacheData(random);

        final long startTime = nanoTime();
        for (int index = 1; index <= totalCount; index++) {
            final long userId = random.nextLong();
            executorService.submit(() -> {
                try {
                    final byte[] blacklistBytes = redisTemplate.opsForValue().get(getBlacklistKey(userId));
                    if (blacklistBytes != null) {
                        decompressAsObjWithZstd(blacklistBytes, UserCache.class, objectMapper);
                    }
                    final byte[] userCacheBytes = redisTemplate.opsForValue().get(getCacheKey(userId));
                    if (userCacheBytes != null) {
                        decompressAsObjWithZstd(userCacheBytes, UserCache.class, objectMapper);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        final long endTime = nanoTime();
        final long durationInMillis = NANOSECONDS.toMillis(endTime - startTime);
        log.info("Origin duration: {} ms", durationInMillis);
        executorService.shutdown();
    }

    private void initCacheData(final Random random) {
        final List<UserCache> userCaches = new ArrayList<>();
        final List<UserCache> blackList = new ArrayList<>();
        for (int index = 1; index <= 50_000; index++) {
            final long userId = random.nextLong();
            final UserCache userCache = new UserCache(
                userId,
                "user" + index,
                "user" + index + "@example.com",
                "",
                "NORMAL"
            );
            userCaches.add(userCache);
            if (index % 2 == 0) {
                blackList.add(userCache);
            }
        }
        userCacheWriteDao.saveUserCacheBulk(userCaches);
        userCacheWriteDao.saveBlackListCacheBulk(blackList);
    }

    private String getCacheKey(Long userId) {
        return String.format("user:cache:%d", userId);
    }

    private String getBlacklistKey(Long userId) {
        return String.format("user:blacklist:%d", userId);
    }
}
