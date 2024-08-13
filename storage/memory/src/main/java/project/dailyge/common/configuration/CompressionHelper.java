package project.dailyge.common.configuration;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class CompressionHelper {

    private CompressionHelper() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static byte[] compressAsByteArray(final Object obj) {
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
}
