package project.dailyge.test.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static project.dailyge.common.exception.RedisExceptionUtils.resolveRedisSystemException;

@DisplayName("[UnitTest] RedisUtils 단위 테스트")
class RedisExceptionUtilsUnitTest {

    @Test
    @DisplayName("에러 메시지가 NOSCRIPT를 포함하고 있다면 Runnable이 실행된다.")
    void whenMessageContainsNoScriptThenRunnableIsExecuted() {
        final String message = "NOSCRIPT";
        final Runnable runnable = mock(Runnable.class);

        resolveRedisSystemException(message, runnable);

        then(runnable).should(times(1)).run();
    }

    @Test
    @DisplayName("에러 메시지가 NOSCRIPT를 포함하고 있지 않다면 Runnable이 실행되지 않는다.")
    void whenMessageDoesNotContainNoScriptThenRunnableIsNotExecuted() {
        final String message = "Hello Error";
        final Runnable runnable = mock(Runnable.class);

        resolveRedisSystemException(message, runnable);

        then(runnable).should(times(0)).run();
    }
}

