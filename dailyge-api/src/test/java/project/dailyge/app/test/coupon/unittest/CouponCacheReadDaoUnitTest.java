package project.dailyge.app.test.coupon.unittest;

import io.lettuce.core.RedisException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.coupon.persistence.CouponCacheReadDao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.BAD_GATEWAY;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.INTERNAL_SERVER_ERROR;

@DisplayName("[UnitTest] CouponCacheReadDao 단위 테스트")
class CouponCacheReadDaoUnitTest {

    private static final String USER_COUPON_KEY = "user:coupon";
    private RedisTemplate<String, byte[]> redisTemplate;
    private ValueOperations<String, byte[]> tempOperations;
    @InjectMocks
    private CouponCacheReadDao couponCacheReadDao;

    @BeforeEach
    void setUp() {
        redisTemplate = mock(RedisTemplate.class);
        couponCacheReadDao = new CouponCacheReadDao(redisTemplate);
        tempOperations = mock(ValueOperations.class);
    }

    @Test
    @DisplayName("redis 호출 반환값이 true라면 existsByUserId는 true를 반환한다.")
    void whenRedisReturnTrueThenExistsByUserIdReturnTrue() {
        when(redisTemplate.opsForValue()).thenReturn(tempOperations);
        when(tempOperations.getBit(USER_COUPON_KEY, 1L)).thenReturn(true);
        assertTrue(couponCacheReadDao.existsByUserId(1L));
    }

    @Test
    @DisplayName("redis 호출 반환값이 false라면 existsByUserId는 false를 반환한다.")
    void whenRedisReturnFalseThenExistsByUserIdReturnFalse() {
        when(redisTemplate.opsForValue()).thenReturn(tempOperations);
        when(tempOperations.getBit(USER_COUPON_KEY, 1L)).thenReturn(false);
        assertFalse(couponCacheReadDao.existsByUserId(1L));
    }

    @Test
    @DisplayName("redis 호출 시 RedisException을 던지면 existsByUserId는 false를 반환한다.")
    void whenRedisThrowRedisExceptionThenThrowCommonException() {
        final String message = "test1";
        final RedisException exception = new RedisException(message);
        when(redisTemplate.opsForValue()).thenReturn(tempOperations);
        when(tempOperations.getBit(USER_COUPON_KEY, 1L)).thenThrow(exception);
        assertThrows(CommonException.from(exception.getMessage(), BAD_GATEWAY).getClass(), () -> {
            couponCacheReadDao.existsByUserId(1L);
        });
    }

    @Test
    @DisplayName("redis 호출 시 Exception을 던지면 existsByUserId는 false를 반환한다.")
    void whenRedisThrowExceptionThenThrowCommonException() {
        final String message = "test1";
        final RuntimeException exception = new RuntimeException(message);
        when(redisTemplate.opsForValue()).thenReturn(tempOperations);
        when(tempOperations.getBit(USER_COUPON_KEY, 1L)).thenThrow(exception);
        assertThrows(CommonException.from(exception.getMessage(), INTERNAL_SERVER_ERROR).getClass(), () -> {
            couponCacheReadDao.existsByUserId(1L);
        });
    }
}
