package project.dailyge.app.test.coupon.unittest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import project.dailyge.app.core.coupon.persistence.CouponCacheReadDao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("[UnitTest] CouponCacheReadDao 단위 테스트")
public class CouponCacheReadDaoUnitTest {

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
}
