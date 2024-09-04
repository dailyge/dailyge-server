package project.dailyge.app.test.common;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;

import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.common.external.conccurent.LockService;
import project.dailyge.lock.LockUseCase;

@DisplayName("[UnitTest] RedisUtils 단위 테스트")
class RedisUtilsUnitTest {

    private RedissonClient redissonClient;
    private LockUseCase lockUseCase;

    @BeforeEach
    void setUp() {
        redissonClient = mock(RedissonClient.class);
        lockUseCase = new LockService(redissonClient);
    }

    @Test
    @DisplayName("올바르지 않은 파라미터가 들어오면, IllegalArgumentException이 발생한다.")
    void whenInputInvalidParameterThenIllegalArgumentExceptionShouldBeThrown() {
        final Long userId = 1L;
        when(redissonClient.getLock(anyString()))
            .thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> lockUseCase.getLock(userId))
            .isInstanceOf(RuntimeException.class)
            .isInstanceOf(CommonException.class);
    }

    @Test
    @DisplayName("RedisException이 발생하면, ExternalServerException으로 변환된다.")
    void whenRedisExceptionThenExternalServerExceptionShouldBeThrown() {
        when(redissonClient.getLock(anyString()))
            .thenThrow(RedisException.class);

        assertThatThrownBy(() -> lockUseCase.getLock(1L))
            .isInstanceOf(RuntimeException.class)
            .isInstanceOf(CommonException.class);
    }
}
