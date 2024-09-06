package project.dailyge.test.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static project.dailyge.common.configuration.CompressionHelper.compressAsByteArrayWithGZip;
import static project.dailyge.common.configuration.CompressionHelper.compressAsByteArrayWithZstd;
import static project.dailyge.common.configuration.CompressionHelper.compressStringAsByteArray;
import static project.dailyge.common.configuration.CompressionHelper.decompressAsObj;
import static project.dailyge.common.configuration.CompressionHelper.decompressAsObjWithZstd;
import static project.dailyge.common.configuration.CompressionHelper.decompressAsString;
import project.dailyge.core.cache.user.UserCache;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

@DisplayName("[UnitTest] GZip 및 Zstd 압축 단위 테스트")
class CompressionUnitTest {

    private static final Logger log = LoggerFactory.getLogger(CompressionUnitTest.class);
    private String jwtToken;
    private UserCache userCache;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        final String imgUrl = "https//shorturl.at/D5sge";

        // 샘플 JWT 토큰
        jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        userCache = new UserCache(1L, "dailyge", "dailyge@gmail.com", imgUrl, "NORMAL");
    }

    @Test
    @DisplayName("GZip으로 압축된 문자열 데이터를 압축 해제하면 결과가 같다.")
    void whenDecompressGZipStrAfterCompressionThenResultShouldBeSame() {
        final String expected = jwtToken;
        final byte[] compressedData = compressStringAsByteArray(expected.getBytes(UTF_8));
        final String decompressedData = decompressAsString(compressedData);

        assertEquals(expected, decompressedData);
    }

    @Test
    @DisplayName("GZip으로 압축된 객체 데이터를 압축 해제하면 결과가 같다.")
    void whenDecompressGZipObjAfterCompressionThenResultShouldBeSame() {
        final byte[] compressedData = compressAsByteArrayWithGZip(userCache);
        final UserCache decompressedData = decompressAsObj(compressedData, UserCache.class);

        assertEquals(userCache, decompressedData);
    }

    @Test
    @DisplayName("Zstd로 압축된 객체 데이터를 압축 해제하면 결과가 같다.")
    void whenDecompressZstdObjAfterCompressionThenResultShouldBeSame() {
        final byte[] compressedData = compressAsByteArrayWithZstd(userCache, objectMapper);
        final UserCache decompressedData = decompressAsObjWithZstd(compressedData, UserCache.class, objectMapper);

        assertEquals(userCache, decompressedData);
    }

    @Test
    @DisplayName("GZip 빈 배열을 압축 해제하면 RuntimeException이 발생한다.")
    void whenDecompressEmptyArrayThenRuntimeExceptionShouldBeThrown() {
        assertThrows(RuntimeException.class, () -> decompressAsString(new byte[0]));
    }

    @Test
    @DisplayName("Zstd Null 데이터를 압축하면 RuntimeException이 발생한다.")
    void whenCompressNullWithZstdThenRuntimeExceptionShouldBeThrown() {
        assertThrows(RuntimeException.class, () -> compressAsByteArrayWithZstd(null, objectMapper));
    }

    @Test
    @DisplayName("GZip 문자열 데이터를 압축하면 사이즈가 작아진다.")
    void whenCompressGZipStringThenDataSizeShouldBeSmaller() {
        final byte[] originalData = serialize(jwtToken);
        final byte[] compressedData = compressStringAsByteArray(jwtToken.getBytes(UTF_8));

        assertTrue(compressedData.length < originalData.length);

        log.info("GZip Original Data Size: {}", originalData.length);
        log.info("GZip Compressed Data Size: {}", compressedData.length);
    }

    @Test
    @DisplayName("Zstd 객체 데이터를 압축하면 사이즈가 작아진다.")
    void whenCompressZstdObjectThenDataSizeShouldBeSmaller() {
        final byte[] originalData = serialize(userCache);

        final byte[] compressedData = compressAsByteArrayWithZstd(userCache, objectMapper);

        assertTrue(compressedData.length < originalData.length);

        log.info("Zstd Original Data Size: {}", originalData.length);
        log.info("Zstd Compressed Data Size: {}", compressedData.length);
    }

    @Test
    @DisplayName("GZip 너무 작은 데이터를 압축하면, 메타 데이터로 인해 데이터 크기가 오히려 커진다.")
    void whenCompressTooSmallDataThenSizeShouldBeBigger() {
        final Long userId = 1L;
        final byte[] originalData = serialize(userId);
        final byte[] compressedData = compressAsByteArrayWithGZip(userId);

        assertTrue(compressedData.length > originalData.length);

        log.info("Original Data Size: {}", originalData.length);
        log.info("Compressed Data Size: {}", compressedData.length);
    }


    @Test
    @DisplayName("GZip과 Zstd 압축 및 해제 속도 비교")
    void whenCompareGZipAndZstdThenCheckPerformance() {
        for (int count = 1; count <= 5; count++) {
            // GZip Compression
            final long gzipCompressStartTime = System.nanoTime();
            final byte[] gzipCompressedData = compressAsByteArrayWithGZip(userCache);
            final long gzipCompressEndTime = System.nanoTime();
            final long gzipCompressionTime = gzipCompressEndTime - gzipCompressStartTime;

            // Zstd Compression
            final long zstdCompressStartTime = System.nanoTime();
            final byte[] zstdCompressedData = compressAsByteArrayWithZstd(userCache, objectMapper);
            final long zstdCompressEndTime = System.nanoTime();
            final long zstdCompressionTime = zstdCompressEndTime - zstdCompressStartTime;

            // GZip Decompression
            final long gzipDecompressStartTime = System.nanoTime();
            decompressAsObj(gzipCompressedData, UserCache.class);
            final long gzipDecompressEndTime = System.nanoTime();
            final long gzipDecompressionTime = gzipDecompressEndTime - gzipDecompressStartTime;

            // Zstd Decompression
            final long zstdDecompressStartTime = System.nanoTime();
            decompressAsObjWithZstd(zstdCompressedData, UserCache.class, objectMapper);
            final long zstdDecompressEndTime = System.nanoTime();
            final long zstdDecompressionTime = zstdDecompressEndTime - zstdDecompressStartTime;

            // Logging the results
            log.info("GZip Compression Time: {} ms", gzipCompressionTime / 1_000_000.0);
            log.info("Zstd Compression Time: {} ms", zstdCompressionTime / 1_000_000.0);
            log.info("GZip Decompression Time: {} ms", gzipDecompressionTime / 1_000_000.0);
            log.info("Zstd Decompression Time: {} ms", zstdDecompressionTime / 1_000_000.0);
            log.info("--------------------------------------------------------x>");
        }
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
