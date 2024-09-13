package project.dailyge.common.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.luben.zstd.Zstd;
import static com.github.luben.zstd.Zstd.isError;
import com.github.luben.zstd.ZstdDictCompress;
import com.github.luben.zstd.ZstdDictDecompress;
import com.github.luben.zstd.ZstdInputStream;
import com.github.luben.zstd.ZstdOutputStream;
import static java.nio.charset.StandardCharsets.UTF_8;
import project.dailyge.core.cache.user.UserCache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class CompressionHelper {

    private static final byte[] zstdDictionary;
    private static final int COMPRESSION_LEVEL = 5;
    private static final int START = 1;
    private static final int END = 1_000;
    private static final int DICTIONARY_SIZE = 1_024;

    static {
        zstdDictionary = trainZstdDictionary(createTrainingData(), new ObjectMapper());
    }

    private CompressionHelper() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static byte[] compressAsByteArrayWithZstd(
        final Object obj,
        final ObjectMapper objectMapper
    ) {
        try (
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final ZstdDictCompress dictCompress = new ZstdDictCompress(zstdDictionary, COMPRESSION_LEVEL);
            final ZstdOutputStream zos = new ZstdOutputStream(bos).setDict(dictCompress)
        ) {
            if (obj == null) {
                throw new IllegalArgumentException("Cannot compress null object.");
            }
            final byte[] byteArray = objectMapper.writeValueAsBytes(obj);
            zos.write(byteArray);
            zos.close();
            return bos.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to compress data with Zstd using dictionary.", ex);
        }
    }

    public static byte[] compressAsByteArrayWithGZip(final Object obj) {
        try (
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final GZIPOutputStream gos = new GZIPOutputStream(bos);
            final ObjectOutputStream oos = new ObjectOutputStream(gos)
        ) {
            if (obj == null) {
                throw new IllegalArgumentException("Cannot compress null object.");
            }
            oos.writeObject(obj);
            oos.flush();
            gos.finish();
            return bos.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("Failed compression data.", ex);
        }
    }

    public static byte[] compressStringAsByteArray(final byte[] byteArray) {
        try (
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final GZIPOutputStream gos = new GZIPOutputStream(bos)
        ) {
            gos.write(byteArray);
            gos.close();
            return bos.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("Failed compression data.", ex);
        }
    }

    public static <T> T decompressAsObjWithZstd(
        final byte[] byteArray,
        final Class<T> clazz,
        final ObjectMapper objectMapper
    ) {
        if (byteArray.length == 0) {
            throw new IllegalArgumentException("Cannot decompress empty data.");
        }
        try (
            final ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
            final ZstdDictDecompress dictDecompress = new ZstdDictDecompress(zstdDictionary);
            final ZstdInputStream zis = new ZstdInputStream(bis).setDict(dictDecompress)
        ) {
            byte[] decompressedData = zis.readAllBytes();
            return objectMapper.readValue(decompressedData, clazz);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to decompress data with Zstd using dictionary.", ex);
        }
    }

    public static <T> T decompressAsObj(
        final byte[] byteArray,
        final Class<T> clazz
    ) {
        try (
            final ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
            final GZIPInputStream gis = new GZIPInputStream(bis);
            final ObjectInputStream ois = new ObjectInputStream(gis)
        ) {
            return clazz.cast(ois.readObject());
        } catch (Exception ex) {
            throw new RuntimeException("Failed decompression data.", ex);
        }
    }

    public static String decompressAsString(final byte[] byteArray) {
        try (
            final ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
            final GZIPInputStream gis = new GZIPInputStream(bis)
        ) {
            final byte[] decompressedData = gis.readAllBytes();
            return new String(decompressedData, UTF_8);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to decompress data", ex);
        }
    }

    private static List<UserCache> createTrainingData() {
        final List<UserCache> userCaches = new ArrayList<>();
        for (int index = START; index <= END; index++) {
            final UserCache cache = createUserCache(index);
            userCaches.add(cache);
        }
        return userCaches;
    }

    private static UserCache createUserCache(final int index) {
        return new UserCache(
            (long) index,
            "user" + index,
            "user" + index + "@gmail.com",
            "https://shorturl.at/D5sge",
            "NORMAL"
        );
    }

    private static byte[] trainZstdDictionary(
        final List<UserCache> trainingData,
        final ObjectMapper objectMapper
    ) {
        final List<byte[]> serializedData = new ArrayList<>();
        for (final UserCache userCache : trainingData) {
            try {
                byte[] jsonData = objectMapper.writeValueAsBytes(userCache);
                serializedData.add(jsonData);
            } catch (Exception ex) {
                throw new RuntimeException("Serialization failed during dictionary training", ex);
            }
        }
        final byte[][] input = serializedData.toArray(new byte[0][]);
        final byte[] dict = new byte[DICTIONARY_SIZE];
        final long result = Zstd.trainFromBuffer(input, dict);
        if (isError(result)) {
            throw new RuntimeException("Zstd dictionary training failed: " + Zstd.getErrorName(result));
        }
        return dict;
    }
}
