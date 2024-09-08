package project.dailyge.app.test.coupon.unittest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.coupon.persistence.CouponEventWriteDao;
import project.dailyge.core.cache.coupon.CouponCache;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;

@DisplayName("[UnitTest] CouponCacheWriteDao 단위 테스트")
class CouponEventWriteDaoUnitTest {

    private RedisTemplate<String, byte[]> redisTemplate;
    private ObjectMapper objectMapper;
    private ValueOperations<String, byte[]> tempOperations;
    @InjectMocks
    private CouponEventWriteDao couponEventWriteDao;

    @BeforeEach
    void setUp() {
        redisTemplate = mock(RedisTemplate.class);
        couponEventWriteDao = new CouponEventWriteDao(redisTemplate, objectMapper);
        tempOperations = mock(ValueOperations.class);
    }

    @Test
    @DisplayName("redis 호출 시 RedisException을 던지면 existsByUserId는 false를 반환한다.")
    void whenRedisThrowRedisExceptionThenThrowCommonException() {
        final CouponCache couponCache = new CouponCache(1L, System.currentTimeMillis());
        final List<CouponCache> couponCaches = List.of(couponCache);
        final String message = "test1";
        final RedisException exception = new RedisException(message);
        when(redisTemplate.execute(any(), eq(true))).thenThrow(exception);
        assertThrows(CommonException.from(exception.getMessage(), BAD_GATEWAY).getClass(), () -> {
            couponEventWriteDao.saveBulks(couponCaches);
        });
    }

    @Test
    @DisplayName("redis 호출 시 Exception을 던지면 existsByUserId는 false를 반환한다.")
    void whenRedisThrowExceptionThenThrowCommonException() {
        final CouponCache couponCache = new CouponCache(1L, System.currentTimeMillis());
        final List<CouponCache> couponCaches = List.of(couponCache);
        final String message = "test1";
        final RuntimeException exception = new RuntimeException(message);
        when(redisTemplate.execute(any(), eq(true))).thenThrow(exception);
        assertThrows(CommonException.from(exception.getMessage(), INTERNAL_SERVER_ERROR).getClass(), () -> {
            couponEventWriteDao.saveBulks(couponCaches);
        });
    }

}
