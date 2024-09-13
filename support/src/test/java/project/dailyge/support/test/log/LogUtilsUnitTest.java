package project.dailyge.support.test.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static project.dailyge.app.constant.LogConstant.INFO;
import project.dailyge.app.utils.LogUtils;
import static project.dailyge.app.utils.LogUtils.createLogMessage;

import java.time.LocalDateTime;

@DisplayName("[UnitTest] LogUtils 단위 테스트")
class LogUtilsUnitTest {

    private static ObjectMapper objectMapper;
    private LocalDateTime time;

    @BeforeEach
    void setUp() {
        time = LocalDateTime.of(2024, 9, 11, 12, 0, 0);
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("createLogMessage로 올바른 형식의 로그 메시지를 반환할 수 있다.")
    void whenCreateLogMessageCalledThenReturnCorrectLog() throws JsonProcessingException {
        final String expectedLog = "{\"order\":\"1\", \"layer\":\"PRESENTATION\", \"path\":\"/api/test\", \"method\":\"GET\", \"traceId\":\"123\", "
            + "\"ip\":\"127.0.0.1\", \"visitor\":{\"userId\":null, \"role\":\"GUEST\"}, "
            + "\"time\":\"2024-09-11T12:00:00.000\", \"duration\":\"100ms\", \"context\":{\"args\":null, \"result\":null}}";
        final String actualLog = createLogMessage(
            1,
            "PRESENTATION",
            "/api/test",
            "GET",
            "123",
            "127.0.0.1",
            time,
            100L,
            null,
            null,
            null,
            INFO
        );
        assertEquals(objectMapper.readTree(expectedLog), objectMapper.readTree(actualLog));
    }

    @Test
    @DisplayName("createGuestLogMessage로 GUEST 로그 메시지를 반환할 수 있다.")
    void whenCreateGuestLogMessageThenReturnCorrectGuestLog() throws JsonProcessingException {
        final String expected = "{\"order\":\"1\", \"layer\":\"PRESENTATION\", \"path\":\"/api/test\", \"method\":\"GET\", \"traceId\":\"123\", \"ip\":\"127.0.0.1\", \"visitor\":{\"userId\":null, \"role\":\"GUEST\"}, \"time\":\"2024-09-11T12:00:00.000\", \"duration\":\"100ms\", \"context\":{\"args\":null, \"result\":null}}";
        final String actual = LogUtils.createGuestLogMessage(
            1,
            "PRESENTATION",
            "/api/test",
            "GET",
            "123",
            "127.0.0.1",
            LocalDateTime.of(2024, 9, 11, 12, 0),
            100L,
            null,
            null,
            INFO
        );
        assertEquals(objectMapper.readTree(expected), objectMapper.readTree(actual));
    }

    @Test
    @DisplayName("getVisitor가 null을 입력받았을 때 GUEST JSON을 반환한다.")
    void whenGetVisitorCalledWithNullThenReturnGuest() throws Exception {
        final String expected = "{ \"userId\":null, \"role\":\"GUEST\" }";
        final String actual = LogUtils.getVisitor(null);
        assertEquals(objectMapper.readTree(expected), objectMapper.readTree(actual));
    }

    @Test
    @DisplayName("JSON 문자열에서 Visitor 객체로의 역직렬화가 올바르게 수행된다.")
    void whenDeserializedThenResultShouldBeSame() throws Exception {
        final String json = "{ \"userId\":null, \"role\":\"GUEST\" }";
        final LogUtils.Visitor actual = objectMapper.readValue(json, LogUtils.Visitor.class);
        final LogUtils.Visitor expected = new LogUtils.Visitor();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getVisitor가 사용자 정보가 있을 경우 올바른 값을 반환한다.")
    void whenGetVisitorCalledWithVisitorThenReturnVisitor() {
        final String expected = "{ \"userId\":123, \"role\":\"USER\" }";
        final String actual = LogUtils.getVisitor(expected);
        assertEquals(expected, actual);
    }
}
