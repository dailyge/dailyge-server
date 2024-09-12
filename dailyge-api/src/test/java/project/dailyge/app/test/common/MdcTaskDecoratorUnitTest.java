package project.dailyge.app.test.common;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import static org.springframework.web.context.request.RequestContextHolder.getRequestAttributes;
import static org.springframework.web.context.request.RequestContextHolder.resetRequestAttributes;
import static org.springframework.web.context.request.RequestContextHolder.setRequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import project.dailyge.app.common.log.MdcTaskDecorator;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

@DisplayName("[UnitTest] MdcFilter 단위 테스트")
class MdcTaskDecoratorUnitTest {

    @Test
    @DisplayName("MDC 필터 작업 후, 모든 문맥이 초기화 된다.")
    void whenMdcAfterWorkThenContextShouldBeClear() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        setRequestAttributes(attributes);
        MDC.put("key", "value");

        final Runnable runnable = () -> {
            assertSame(attributes, getRequestAttributes());
            assertEquals("value", MDC.get("key"));
        };
        final Runnable decoratedRunnable = new MdcTaskDecorator()
            .decorate(runnable);
        final ExecutorService executor = newSingleThreadExecutor();
        try {
            final Future<?> future = executor.submit(decoratedRunnable);
            future.get();
        } catch (InterruptedException | ExecutionException ex) {
            fail("Ex:", ex);
        } finally {
            executor.shutdown();
        }

        resetRequestAttributes();
        MDC.clear();
        assertAll(
            () -> assertNull(getRequestAttributes()),
            () -> assertNull(MDC.getCopyOfContextMap())
        );
    }

    @Test
    @DisplayName("멀티 쓰레드 환경에서도 MDC 필터 문맥이 복사되며, 작업이 끝나면 모든 문맥은 초기화 된다.")
    void whenAsyncThenMdcFilterContextShouldBeCopiedAndAllContextMustBeClearedAfterWork() throws InterruptedException {
        final ExecutorService executor = Executors.newFixedThreadPool(8);

        IntStream.range(0, 100).forEach(index -> executor.submit(() -> {
            final MockHttpServletRequest request = new MockHttpServletRequest();
            final ServletRequestAttributes attributes = new ServletRequestAttributes(request);
            setRequestAttributes(attributes);
            final String key = "key";
            final String value = "value" + index;
            MDC.put(key, value);

            final Runnable runnable = () -> {
                assertSame(attributes, getRequestAttributes());
                assertEquals(value, MDC.get(key));
            };

            final Runnable decoratedRunnable = new MdcTaskDecorator().decorate(runnable);
            decoratedRunnable.run();

            assertNull(getRequestAttributes());
            assertTrue(MDC.getCopyOfContextMap() == null || MDC.getCopyOfContextMap().isEmpty());
        }));

        executor.shutdown();
        if (!executor.awaitTermination(5, SECONDS)) {
            fail("Time over.");
        }
    }
}
