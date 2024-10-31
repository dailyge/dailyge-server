package project.dailyge.entity.test.codeandmessage;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.codeandmessage.CodeAndMessageJpaEntity;

@DisplayName("[UnitTest] CodeAndMessage 단위 테스트")
class CodeAndMessageUnitTest {

    @Test
    @DisplayName("정적 메서드를 통해 CodeAndMessage 객체를 생성할 수 있다.")
    void whenCreateCodeAndMessageWithStaticMethodThenObjectShouldBeNotNull() {
        final CodeAndMessageJpaEntity entity = CodeAndMessageJpaEntity.Companion.create(
            "common",
            "BAD_REQUEST",
            400,
            "올바른 파라미터를 입력해주세요."
        );
        assertNotNull(entity);
    }

    @Test
    @DisplayName("Source를 업데이트하면 Target과 결과가 같아진다.")
    void whenUpdateSourceThenResultShouldBeSameAsTarget() {
        final CodeAndMessageJpaEntity source = CodeAndMessageJpaEntity.Companion.create(
            "common",
            "BAD_REQUEST",
            400,
            "올바른 파라미터를 입력해주세요."
        );

        final CodeAndMessageJpaEntity target = CodeAndMessageJpaEntity.Companion.create(
            "common",
            "INTERNAL_SERVER_ERROR",
            500,
            "서버 내부 오류 입니다."
        );

        source.update(target);

        assertAll(
            () -> assertEquals(target.getDomain(), source.getDomain()),
            () -> assertEquals(target.getCode(), source.getCode()),
            () -> assertEquals(target.getName(), source.getName()),
            () -> assertEquals(target.getMessage(), source.getMessage())
        );
    }
}
