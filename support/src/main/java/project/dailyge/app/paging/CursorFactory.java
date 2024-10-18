package project.dailyge.app.paging;

public final class CursorFactory {

    private static final int ZERO = 0;
    private static final int DEFAULT_LIMIT = 10;
    private static final int PAGE_LIMIT = 25;

    private CursorFactory() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static Cursor createCursor(
        final String index,
        final String limit
    ) {
        return Cursor.createCursor(
            getIndex(index),
            getLimit(limit)
        );
    }

    private static Long getIndex(final String index) {
        final Long parsedIndex = parseIndex(index);
        if (parsedIndex == null) {
            return null;
        }
        if (parsedIndex <= ZERO) {
            return null;
        }
        return parsedIndex;
    }

    private static Long parseIndex(final String index) {
        if (index == null) {
            return null;
        }
        try {
            return Long.parseLong(index);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private static Integer getLimit(final String limit) {
        final Integer parsedLimit = parseLimit(limit);
        if (limit == null) {
            return DEFAULT_LIMIT;
        }
        if (parsedLimit < ZERO || parsedLimit > PAGE_LIMIT) {
            return DEFAULT_LIMIT;
        }
        return parsedLimit;
    }

    private static Integer parseLimit(final String limit) {
        if (limit == null) {
            return DEFAULT_LIMIT;
        }
        try {
            return Integer.parseInt(limit);
        } catch (NumberFormatException exception) {
            return DEFAULT_LIMIT;
        }
    }
}
