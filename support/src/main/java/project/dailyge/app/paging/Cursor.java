package project.dailyge.app.paging;

import java.util.Objects;

public class Cursor {

    private final Long index;
    private final Integer limit;

    private Cursor(
        final Long index,
        final Integer limit
    ) {
        this.index = index;
        this.limit = limit;
    }

    public static Cursor createCursor(
        final Long index,
        final Integer limit
    ) {
        return new Cursor(index, limit);
    }

    public Long getIndex() {
        return index;
    }

    public Integer getLimit() {
        return limit;
    }

    public boolean isNull() {
        return index == null;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        final Cursor cursor = (Cursor) object;
        return Objects.equals(index, cursor.index) && Objects.equals(limit, cursor.limit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, limit);
    }

    @Override
    public String toString() {
        return String.format("{\"index\": \"%s\", \"limit\": \"%s\"}", index, limit);
    }
}
