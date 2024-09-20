package project.dailyge.document.log;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@DisplayName("[UnitTest] OperationLogDocument 단위 테스트")
class OperationLogDocumentUnitTest {

    @Test
    @DisplayName("OperationLogDocument 객체를 생성할 수 있다.")
    void whenCreateOperationLogDocumentThenObjectIsCorrect() {
        final String order = "1";
        final String layer = "controller";
        final String path = "/api/test";
        final String method = "GET";
        final String traceId = "trace-12345";
        final String ip = "192.168.1.1";
        final String visitor = "123";
        final LocalDateTime time = LocalDateTime.now();
        final String duration = "100ms";
        final Object context = new Object();
        final String level = "INFO";

        final OperationLogDocument logDocument = new OperationLogDocument(
            order, layer, path, method, traceId, ip, visitor, time, duration, context, level
        );

        assertAll(
            () -> assertNotNull(logDocument),
            () -> assertNotNull(logDocument.getId()),
            () -> assertNotEquals(traceId, logDocument.getId()),
            () -> assertEquals(order, logDocument.getOrder()),
            () -> assertEquals(layer, logDocument.getLayer()),
            () -> assertEquals(path, logDocument.getPath()),
            () -> assertEquals(method, logDocument.getMethod()),
            () -> assertEquals(traceId, logDocument.getTraceId()),
            () -> assertEquals(ip, logDocument.getIp()),
            () -> assertEquals(visitor, logDocument.getVisitor()),
            () -> assertEquals(time, logDocument.getTime()),
            () -> assertEquals(duration, logDocument.getDuration()),
            () -> assertEquals(context, logDocument.getContext()),
            () -> assertEquals(level, logDocument.getLevel())
        );
    }
}
