package project.dailyge.document.log;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@DisplayName("[UnitTest] SystemLogDocument 단위 테스트")
class SystemLogDocumentUnitTest {

    @Test
    @DisplayName("SystemLogDocument 객체를 생성할 수 있다.")
    void whenCreateSystemLogDocumentThenObjectIsCorrect() {
        final String id = "log-12345";
        final LocalDateTime time = LocalDateTime.now();
        final String level = "INFO";
        final String systemLog = "System started successfully.";

        final SystemLogDocument logDocument = new SystemLogDocument(id, time, level, systemLog);

        assertAll(
            () -> assertNotNull(logDocument),
            () -> assertEquals(id, logDocument.getId()),
            () -> assertEquals(time, logDocument.getTime()),
            () -> assertEquals(level, logDocument.getLevel()),
            () -> assertEquals(systemLog, logDocument.getSystemLog())
        );
    }
}
