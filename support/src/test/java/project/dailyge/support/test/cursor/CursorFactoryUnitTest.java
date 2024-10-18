package project.dailyge.support.test.cursor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.paging.Cursor;
import project.dailyge.app.paging.CursorFactory;

@DisplayName("[UnitTest] CursorFactory 단위 테스트")
class CursorFactoryUnitTest {

    @Test
    @DisplayName("유효한 index와 limit로 Cursor를 생성한다.")
    void whenValidIndexAndLimitThenCreateCursor() {
        final String index = "5";
        final String limit = "10";
        final Cursor cursor = CursorFactory.createCursor(index, limit);

        assertNotNull(cursor);
        assertEquals(5L, cursor.getIndex());
        assertEquals(10, cursor.getLimit());
    }

    @Test
    @DisplayName("유효하지 않은 index로 Cursor를 생성하면 index는 null이다.")
    void whenInvalidIndexThenCursorIndexIsNull() {
        final String invalidIndex = "invalid";
        final String limit = "10";
        final Cursor cursor = CursorFactory.createCursor(invalidIndex, limit);
        assertNotNull(cursor);
        assertNull(cursor.getIndex());
        assertEquals(10, cursor.getLimit());
    }

    @Test
    @DisplayName("유효하지 않은 limit로 Cursor를 생성하면 기본 limit값이 사용된다.")
    void whenInvalidLimitThenDefaultLimitIsUsed() {
        final String index = "5";
        final String invalidLimit = "invalid";
        final Cursor cursor = CursorFactory.createCursor(index, invalidLimit);
        assertNotNull(cursor);
        assertEquals(5L, cursor.getIndex());
        assertEquals(10, cursor.getLimit());
    }

    @Test
    @DisplayName("limit이 null일 때 기본 limit값이 사용된다.")
    void whenLimitIsNullThenDefaultLimitIsUsed() {
        final String index = "5";
        final String limit = null;
        final Cursor cursor = CursorFactory.createCursor(index, limit);
        assertNotNull(cursor);
        assertEquals(5L, cursor.getIndex());
        assertEquals(10, cursor.getLimit());
    }

    @Test
    @DisplayName("index가 null일 때 Cursor의 index는 null이다.")
    void whenIndexIsNullThenCursorIndexIsNull() {
        final String index = null;
        final String limit = "10";
        final Cursor cursor = CursorFactory.createCursor(index, limit);
        assertNotNull(cursor);
        assertNull(cursor.getIndex());
        assertEquals(10, cursor.getLimit());
    }

    @Test
    @DisplayName("limit이 0 이하이거나 페이지 제한을 초과할 경우 기본 limit값이 사용된다.")
    void whenLimitOutOfRangeThenDefaultLimitIsUsed() {
        final String index = "5";
        String limit = "-1";
        Cursor cursor = CursorFactory.createCursor(index, limit);

        assertNotNull(cursor);
        assertEquals(5L, cursor.getIndex());
        assertEquals(10, cursor.getLimit());

        limit = "100";
        cursor = CursorFactory.createCursor(index, limit);

        assertNotNull(cursor);
        assertEquals(5L, cursor.getIndex());
        assertEquals(10, cursor.getLimit());
    }
}
