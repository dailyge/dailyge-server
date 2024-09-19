package project.dailyge.document.common;

import com.fasterxml.uuid.Generators;

public final class UuidGenerator {

    private UuidGenerator() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static String createTimeBasedUUID() {
        return Generators.timeBasedGenerator().generate().toString();
    }

    public static long createTimeStamp() {
        long leastSignificantBits = Generators.timeBasedGenerator().generate().getLeastSignificantBits();
        long timeLow = leastSignificantBits >>> 32;
        long timeMid = (leastSignificantBits & 0xFFFF0000L) >>> 16;
        long timeHighAndVersion = (leastSignificantBits & 0x0FFF000000000000L) >>> 48;
        return (timeHighAndVersion << 48) | (timeMid << 32) | timeLow;
    }
}
