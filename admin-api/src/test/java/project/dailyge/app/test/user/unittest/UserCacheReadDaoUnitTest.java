package project.dailyge.app.test.user.unittest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import project.dailyge.app.common.exception.ExternalServerException;
import project.dailyge.app.user.persistence.UserCacheReadDao;

@DisplayName("[UnitTest] UserCacheReadDao Unit Test")
class UserCacheReadDaoUnitTest {

    @Mock
    private RedisTemplate<String, byte[]> redisTemplate;

    @Mock
    private ValueOperations<String, byte[]> valueOperations;

    @InjectMocks
    private UserCacheReadDao userCacheReadDao;

    private static final Long USER_ID = 1L;
    private static final String CACHE_KEY = "user:cache:1";

    @BeforeEach
    void setUp() {
        openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("Redis에서 사용자 데이터를 찾지 못하면 null을 반환한다.")
    void whenFindByIdIsNotFoundThenReturnNull() {
        when(valueOperations.get(CACHE_KEY)).thenReturn(null);
        assertNull(userCacheReadDao.findById(USER_ID));
    }

    @Test
    @DisplayName("Redis에서 오류가 발생하면 ExternalServerException을 발생시킨다.")
    void whenFindByIdThrowsRedisExceptionThenThrowExternalServerException() {
        when(valueOperations.get(anyString())).thenThrow(new RuntimeException("Redis Exception"));
        assertThrows(ExternalServerException.class, () -> userCacheReadDao.findById(USER_ID));
    }

    @Test
    @DisplayName("사용자 캐시 존재 여부 확인 시, RedisException 발생 시 ExternalServerException 발생")
    void whenExistsByIdThrowsRedisExceptionThenThrowExternalServerException() {
        when(redisTemplate.hasKey(CACHE_KEY)).thenThrow(new RuntimeException("Redis Exception"));
        assertThrows(ExternalServerException.class, () -> userCacheReadDao.existsById(USER_ID));
    }

    @Test
    @DisplayName("사용자 캐시 존재 여부 확인 시, Redis에 데이터가 있으면 true를 반환한다.")
    void whenExistsByIdThenReturnTrue() {
        when(redisTemplate.hasKey(CACHE_KEY)).thenReturn(true);
        assertTrue(userCacheReadDao.existsById(USER_ID));
    }

    @Test
    @DisplayName("존재 여부 확인에서 Redis에 데이터가 없으면 false를 반환한다.")
    void whenExistsByIdThenReturnFalse() {
        when(redisTemplate.hasKey(CACHE_KEY)).thenReturn(false);
        assertFalse(userCacheReadDao.existsById(USER_ID));
    }
}
