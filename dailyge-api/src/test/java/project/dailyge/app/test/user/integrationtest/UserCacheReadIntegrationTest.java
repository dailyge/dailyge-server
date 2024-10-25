package project.dailyge.app.test.user.integrationtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import static java.lang.System.nanoTime;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.user.persistence.UserCacheWriteDao;
import static project.dailyge.common.configuration.CompressionHelper.compressAsByteArrayWithZstd;
import static project.dailyge.common.configuration.CompressionHelper.decompressAsObjWithZstd;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheReadService;

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
    private UserCacheReadService userCacheReadService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate<String, byte[]> redisTemplate;

    @Autowired
    private String scriptSha;

    private final String script = "local cache_key = KEYS[1] "
        + "local blacklist_key = KEYS[2] "
        + "local cache = redis.call('GET', cache_key) "
        + "if not cache then "
        + "    return nil "
        + "end "
        + "if redis.call('EXISTS', blacklist_key) == 1 then "
        + "    return nil "
        + "end "
        + "return cache ";

    @AfterEach
    @Override
    protected void afterEach() {
        flushScript();
        loadScriptAndGetSha();
    }

    @Test
    @DisplayName("레디스 서버 다운으로 스크립트가 초기화가 된 후, 재 등록하면 해시 값은 언제나 동일하다.")
    void givenRedisServerDownAndFlushedScriptWhenReRegisterScriptThenHashMustBeEquals() {
        final String originSha1 = loadScriptAndGetSha();
        for (int index = 1; index <= 100; index++) {
            flushScript();
            final String newSha1 = loadScriptAndGetSha();
            assertEquals(originSha1, newSha1);
        }
    }

    @Test
    @DisplayName("LuaScript를 빈으로 등록하면, 레디스에 등록된다.")
    void whenRegisterLuaScriptThenResultShouldExists() {
        final List<Boolean> result = redisTemplate.execute((RedisCallback<List<Boolean>>) connection ->
            connection.scriptingCommands().scriptExists(scriptSha)
        );
        Assertions.assertNotNull(result);
    }

    @Test
    @DisplayName("존재하지 않는 루아 스크립트를 조회하면 false가 반환된다.")
    void whenNotRegisterLuaScriptThenResultShouldNull() {
        final List<Boolean> invalidResult = redisTemplate.execute((RedisCallback<List<Boolean>>) connection ->
            connection.scriptingCommands().scriptExists("INVALID_SCRIPT")
        );
        assertAll(
            () -> Assertions.assertNotNull(invalidResult),
            () -> Assertions.assertFalse(requireNonNull(invalidResult).isEmpty()),
            () -> Assertions.assertFalse(requireNonNull(invalidResult).get(0))
        );
    }

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
        Assertions.assertNull(userCacheReadService.findById(userCache.getId()));
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
            () -> Assertions.assertNotNull(userCacheReadService.findById(userCache.getId())),
            () -> assertEquals(1L, userCache.getId()),
            () -> assertEquals("dailyge", userCache.getNickname()),
            () -> assertEquals("dailyge@gmail.com", userCache.getEmail()),
            () -> assertEquals("", userCache.getProfileImageUrl())
        );
    }

    @Test
    @DisplayName("등록된 루아 스크립트가 존재하지 않으면 CommonException이 발생한다.")
    void whenScriptNotExistsThenCommonExceptionShouldBeHappen() {
        flushScript();
        assertThrows(CommonException.class, () -> userCacheReadService.findById(1L));
    }

    @Test
    @DisplayName("캐시가 존재하지 않으면, null이 반환된다.")
    void whenNormalUserAndCacheNotExistsThenResultShouldBeNull() {
        Assertions.assertNull(userCacheReadService.findById(Long.MAX_VALUE));
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
        for (int index = 1; index <= 1; index++) {
            final long userId = random.nextLong();
            executorService.submit(() -> {
                try {
                    userCacheReadService.findById(userId);
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

    private void flushScript() {
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.scriptingCommands().scriptFlush();
            return null;
        });
    }

    private String loadScriptAndGetSha() {
        return redisTemplate.execute((RedisCallback<String>) connection ->
            connection.scriptingCommands().scriptLoad(script.getBytes())
        );
    }

    private String getCacheKey(Long userId) {
        return String.format("user:cache:%d", userId);
    }

    private String getBlacklistKey(Long userId) {
        return String.format("user:blacklist:%d", userId);
    }
}
