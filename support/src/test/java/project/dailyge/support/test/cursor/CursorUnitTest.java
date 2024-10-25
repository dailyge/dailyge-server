package project.dailyge.support.test.cursor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.paging.Cursor;

@DisplayName("[UnitTest] Cursor 단위 테스트")
class CursorUnitTest {

    private Cursor cursor;

    @BeforeEach
    void setUp() {
        cursor = Cursor.createCursor(1L, 10);
    }

    @Test
    @DisplayName("Cursor 생성 시 올바르게 초기화된다.")
    void whenCreateCursorThenInitializeCorrectly() {
        final Long index = 1L;
        final Integer limit = 10;
        final Cursor newCursor = Cursor.createCursor(index, limit);

        assertNotNull(newCursor);
        assertEquals(index, newCursor.getIndex());
        assertEquals(limit, newCursor.getLimit());
    }

    @Test
    @DisplayName("Cursor가 null index를 포함하고 있는지 확인한다.")
    void whenCheckIsNullThenReturnCorrectResult() {
        final Cursor nullIndexCursor = Cursor.createCursor(null, 10);
        final boolean isNull = nullIndexCursor.isNull();
        assertTrue(isNull);
    }

    @Test
    @DisplayName("Cursor가 null index가 아닌 경우 false를 반환한다.")
    void whenCheckIsNotNullThenReturnFalse() {
        final boolean isNull = cursor.isNull();
        assertFalse(isNull);
    }

    @Test
    @DisplayName("Cursor 객체가 동일한 속성을 가지는 경우 equals 메서드는 true를 반환한다.")
    void whenEqualCursorsThenReturnTrue() {
        final Cursor newCursor = Cursor.createCursor(1L, 10);
        final boolean isEqual = this.cursor.equals(newCursor);
        assertTrue(isEqual);
    }

    @Test
    @DisplayName("Cursor 객체가 다른 속성을 가지는 경우 equals 메서드는 false를 반환한다.")
    void whenDifferentCursorsThenReturnFalse() {
        final Cursor newCursor = Cursor.createCursor(2L, 10);
        final boolean isEqual = this.cursor.equals(newCursor);
        assertFalse(isEqual);
    }

    @Test
    @DisplayName("Cursor 객체가 동일한 속성을 가지는 경우 동일한 해시코드를 반환한다.")
    void whenEqualCursorsThenReturnSameHashCode() {
        final Cursor newCursor = Cursor.createCursor(1L, 10);
        final int hashCode1 = this.cursor.hashCode();
        final int hashCode2 = newCursor.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    @DisplayName("Cursor 객체가 다른 속성을 가지는 경우 다른 해시코드를 반환한다.")
    void whenDifferentCursorsThenReturnDifferentHashCode() {
        final Cursor newCursor = Cursor.createCursor(2L, 10);
        final int hashCode1 = this.cursor.hashCode();
        final int hashCode2 = newCursor.hashCode();
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    @DisplayName("Cursor 객체의 toString 메서드는 올바른 JSON 형식의 문자열을 반환한다.")
    void whenToStringCalledThenReturnJsonFormatString() {
        assertEquals("{\"index\": \"1\", \"limit\": \"10\"}", cursor.toString());
    }
}
