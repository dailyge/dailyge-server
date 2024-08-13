package project.dailyge.test.common;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static project.dailyge.common.configuration.CompressionHelper.compressAsByteArray;
import static project.dailyge.common.configuration.CompressionHelper.compressStringAsByteArray;
import static project.dailyge.common.configuration.CompressionHelper.decompressAsObj;
import static project.dailyge.common.configuration.CompressionHelper.decompressAsString;
import project.dailyge.core.cache.user.UserCache;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

@DisplayName("[UnitTest] 압축 단위 테스트")
class CompressionUnitTest {

    private static final Logger log = LoggerFactory.getLogger(CompressionUnitTest.class);
    private String jwtToken;
    private UserCache userCache;

    @BeforeEach
    void setUp() {
        final String imgUrl = "https://shorturl.at/D5sge";

        // https://jwt.io/
        jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        userCache = new UserCache(1L, "dailyge", "dailyge@gmail.com", imgUrl, "NORMAL");
    }

    @Test
    @DisplayName("압축된 문자열 데이터를 압축 해제하면 결과가 같다.")
    void whenDecompressStrAfterCompressionThenResultShouldBeSame() {
        final String expected = jwtToken;
        final byte[] compressedData = compressStringAsByteArray(expected.getBytes(UTF_8));
        final String decompressedData = decompressAsString(compressedData);

        assertEquals(expected, decompressedData);
    }

    @Test
    @DisplayName("압축된 객체 데이터를 압축 해제하면 결과가 같다.")
    void whenDecompressObjAfterCompressionThenResultShouldBeSame() {
        final byte[] compressedData = compressAsByteArray(userCache);
        final UserCache decompressedData = decompressAsObj(compressedData, UserCache.class);

        assertEquals(userCache, decompressedData);
    }

    @Test
    @DisplayName("빈 배열을 압축해제하면 RuntimeException이 발생한다.")
    void whenDecompressEmptyArrayThenRuntimeExceptionShouldBeHappen() {
        assertThrows(RuntimeException.class, () -> decompressAsString(new byte[0]));
    }

    @Test
    @DisplayName("Null 데이터를 압축하면 RuntimeException이 발생한다.")
    void whenCompressNullThenRuntimeExceptionShouldBeHappen() {
        assertThrows(RuntimeException.class, () -> compressAsByteArray(null));
    }

    @Test
    @DisplayName("문자열 데이터를 압축하면 사이즈가 작아진다.")
    void whenCompressStringThenDataSizeShouldBeSmaller() {
        final byte[] originalData = serialize(jwtToken);
        final byte[] compressedData = compressStringAsByteArray(jwtToken.getBytes(UTF_8));

        assertTrue(compressedData.length < originalData.length);

        log.info("Original Data Size: {}", originalData.length);
        log.info("Compressed Data Size: {}", compressedData.length);
    }

    @Test
    @DisplayName("객체 데이터를 압축하면 사이즈가 작아진다.")
    void whenCompressObjectThenDataSizeShouldBeSmaller() {
        final byte[] originalData = serialize(userCache);
        final byte[] compressedData = compressAsByteArray(userCache);

        assertTrue(compressedData.length < originalData.length);

        log.info("Original Data Size: {}", originalData.length);
        log.info("Compressed Data Size: {}", compressedData.length);
    }

    @Test
    @DisplayName("너무 작은 데이터를 압축하면, 메타 데이터로 인해 데이터 크기가 오히려 커진다.")
    void whenCompressTooSmallDataThenSizeShouldBeBiggerThanOriginalData() {
        final Long userId = 1L;
        final byte[] originalData = serialize(userId);
        final byte[] compressedData = compressAsByteArray(userId);

        assertTrue(compressedData.length > originalData.length);

        log.info("Original Data Size: {}", originalData.length);
        log.info("Compressed Data Size: {}", compressedData.length);
    }

    private byte[] serialize(final Object obj) {
        try (
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final ObjectOutputStream oos = new ObjectOutputStream(bos)
        ) {
            oos.writeObject(obj);
            oos.flush();
            return bos.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to serialize object.", ex);
        }
    }
}
