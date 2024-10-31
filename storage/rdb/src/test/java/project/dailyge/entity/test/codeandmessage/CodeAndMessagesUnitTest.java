package project.dailyge.entity.test.codeandmessage;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.codeandmessage.CodeAndMessageJpaEntity;
import project.dailyge.entity.codeandmessage.CodeAndMessages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@DisplayName("[UnitTest] CodeAndMessages 단위 테스트")
class CodeAndMessagesUnitTest {

    @Test
    @DisplayName("CodeAndMessages가 비어있다면 필드가 빈 List로 초기화 된다.")
    void whenCodeAndMessagesIsEmptyThenFieldShouldBeEmpty() {
        final CodeAndMessages codeAndMessages = new CodeAndMessages(new ArrayList<>());
        assertTrue(codeAndMessages.isEmpty());
    }

    @Test
    @DisplayName("CodeAndMessages가 null로 초기화 될 경우 필드가 빈 List로 초기화 된다.")
    void whenCodeAndMessagesIsNullThenFieldShouldBeEmpty() {
        final CodeAndMessages codeAndMessages = new CodeAndMessages(null);
        assertTrue(codeAndMessages.isEmpty());
    }

    @Test
    @DisplayName("CodeAndMessages가 올바르게 초기화 될 경우 필드가 올바르게 설정된다.")
    void whenCodeAndMessagesIsInitializedThenFieldShouldBeSetProperly() {
        final List<CodeAndMessageJpaEntity> entities = Arrays.asList(
            CodeAndMessageJpaEntity.Companion.create("common", "OK", 200, "OK"),
            CodeAndMessageJpaEntity.Companion.create("common", "CREATED", 201, "Created")
        );
        final CodeAndMessages codeAndMessages = new CodeAndMessages(entities);

        assertAll(
            () -> assertNotNull(codeAndMessages.getCodeAndMessages()),
            () -> assertEquals(2, codeAndMessages.getCodeAndMessages().size())
        );
    }

    @Test
    @DisplayName("CodeAndMessages를 Map으로 변환할 수 있다.")
    void whenConvertToMapThenResultShouldBeMap() {
        final List<CodeAndMessageJpaEntity> entities = Arrays.asList(
            CodeAndMessageJpaEntity.Companion.create("common", "OK", 200, "OK"),
            CodeAndMessageJpaEntity.Companion.create("common", "CREATED", 201, "Created")
        );
        final CodeAndMessages codeAndMessages = new CodeAndMessages(entities);
        final Map<String, CodeAndMessageJpaEntity> map = codeAndMessages.convertToMap(entities);

        assertAll(
            () -> assertNotNull(map),
            () -> assertEquals(2, map.size()),
            () -> assertEquals("OK", map.get("common:OK").getName()),
            () -> assertEquals("Created", map.get("common:CREATED").getMessage())
        );
    }

    @Test
    @DisplayName("CodeAndMessages를 업데이트하면 기존 엔티티가 업데이트된다.")
    void whenUpdateAllThenEntitiesShouldBeUpdated() {
        final List<CodeAndMessageJpaEntity> initialEntities = Arrays.asList(
            CodeAndMessageJpaEntity.Companion.create("common", "OK", 200, "OK"),
            CodeAndMessageJpaEntity.Companion.create("common", "CREATED", 201, "Created")
        );
        final CodeAndMessages codeAndMessages = new CodeAndMessages(initialEntities);

        final List<CodeAndMessageJpaEntity> newEntities = Arrays.asList(
            CodeAndMessageJpaEntity.Companion.create("common", "OK", 200, "OK"),
            CodeAndMessageJpaEntity.Companion.create("common", "CREATED", 201, "Created")
        );
        codeAndMessages.updateAll(newEntities);

        assertAll(
            () -> assertEquals("OK", codeAndMessages.getCodeAndMessages().get(0).getMessage()),
            () -> assertEquals("Created", codeAndMessages.getCodeAndMessages().get(1).getMessage())
        );
    }

    @Test
    @DisplayName("CodeAndMessages에 새로운 엔티티들을 추가하면 기존 엔티티가 유지된다.")
    void whenUpdateAllWithNewEntitiesThenExistingEntitiesAreMaintained() {
        final List<CodeAndMessageJpaEntity> initialEntities = List.of(
            CodeAndMessageJpaEntity.Companion.create("common", "OK", 200, "OK")
        );
        final CodeAndMessages codeAndMessages = new CodeAndMessages(initialEntities);

        final List<CodeAndMessageJpaEntity> newEntities = List.of(
            CodeAndMessageJpaEntity.Companion.create("common", "CREATED", 201, "Created")
        );
        codeAndMessages.updateAll(newEntities);

        assertAll(
            () -> assertEquals(1, codeAndMessages.getCodeAndMessages().size()),
            () -> assertEquals("OK", codeAndMessages.getCodeAndMessages().get(0).getName())
        );
    }

    @Test
    @DisplayName("존재하지 않는 엔티티를 업데이트하려고 하면 아무 것도 하지 않는다.")
    void whenUpdateAllWithNonExistingEntitiesThenNoChanges() {
        final List<CodeAndMessageJpaEntity> initialEntities = List.of(
            CodeAndMessageJpaEntity.Companion.create("common", "OK", 200, "OK")
        );
        final CodeAndMessages codeAndMessages = new CodeAndMessages(initialEntities);

        final List<CodeAndMessageJpaEntity> newEntities = List.of(
            CodeAndMessageJpaEntity.Companion.create("common", "NEW", 202, "New Entry")
        );
        codeAndMessages.updateAll(newEntities);

        assertAll(
            () -> assertEquals(1, codeAndMessages.getCodeAndMessages().size()),
            () -> assertEquals("OK", codeAndMessages.getCodeAndMessages().get(0).getName())
        );
    }

    @Test
    @DisplayName("CodeAndMessages가 초기화될 때 올바른 엔티티가 설정된다.")
    void whenInitializeThenEntitiesAreSetProperly() {
        final List<CodeAndMessageJpaEntity> entities = List.of(
            CodeAndMessageJpaEntity.Companion.create("common", "OK", 200, "OK"),
            CodeAndMessageJpaEntity.Companion.create("tasks", "TASK_NOT_FOUND", 404, "Task not found")
        );
        final CodeAndMessages codeAndMessages = new CodeAndMessages(entities);

        assertAll(
            () -> assertEquals(2, codeAndMessages.getCodeAndMessages().size()),
            () -> assertEquals("OK", codeAndMessages.getCodeAndMessages().get(0).getName()),
            () -> assertEquals("TASK_NOT_FOUND", codeAndMessages.getCodeAndMessages().get(1).getName())
        );
    }

    @Test
    @DisplayName("CodeAndMessages가 비어있는 경우, updateAll 메서드는 새로운 엔티티들을 추가한다.")
    void whenCodeAndMessagesIsEmptyThenUpdateAllAddsEntities() {
        final CodeAndMessages codeAndMessages = new CodeAndMessages(new ArrayList<>());

        final List<CodeAndMessageJpaEntity> newEntities = Arrays.asList(
            CodeAndMessageJpaEntity.Companion.create("common", "OK", 200, "OK"),
            CodeAndMessageJpaEntity.Companion.create("tasks", "TASK_NOT_FOUND", 404, "Task not found")
        );
        codeAndMessages.updateAll(newEntities);

        assertAll(
            () -> assertEquals(2, codeAndMessages.getCodeAndMessages().size()),
            () -> assertEquals("OK", codeAndMessages.getCodeAndMessages().get(0).getName()),
            () -> assertEquals("TASK_NOT_FOUND", codeAndMessages.getCodeAndMessages().get(1).getName())
        );
    }

    @Test
    @DisplayName("updateAll 호출 시 null을 인자로 전달하면 업데이트를 수행하지 않는다.")
    void whenUpdateAllWithNullThenDoNothing() {
        final List<CodeAndMessageJpaEntity> initialEntities = List.of(
            CodeAndMessageJpaEntity.Companion.create("common", "OK", 200, "OK")
        );
        final CodeAndMessages codeAndMessages = new CodeAndMessages(initialEntities);

        codeAndMessages.updateAll(null);

        assertAll(
            () -> assertEquals(1, codeAndMessages.getCodeAndMessages().size()),
            () -> assertEquals("OK", codeAndMessages.getCodeAndMessages().get(0).getName())
        );
    }
}
