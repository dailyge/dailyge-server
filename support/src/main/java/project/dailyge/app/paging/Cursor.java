package project.dailyge.app.paging;

import lombok.Getter;

import java.util.Objects;

@Getter
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

    public boolean isNull() {
        return index == null;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Cursor cursor)) {
            return false;
        }
        return getIndex().equals(cursor.getIndex()) && getLimit().equals(cursor.getLimit());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIndex(), getLimit());
    }

    @Override
    public String toString() {
        return String.format("{\"index\": \"%s\", \"limit\": \"%s\"}", index, limit);
    }
}
