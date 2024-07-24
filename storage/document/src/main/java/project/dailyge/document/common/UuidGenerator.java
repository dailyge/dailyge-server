package project.dailyge.document.common;

import com.fasterxml.uuid.Generators;

public final class UuidGenerator {

    private UuidGenerator() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static String createTimeBasedUUID() {
        return Generators.timeBasedGenerator().generate().toString();
    }
}
