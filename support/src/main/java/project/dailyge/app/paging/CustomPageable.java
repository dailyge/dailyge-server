package project.dailyge.app.paging;

import lombok.Getter;

@Getter
public class CustomPageable {

    private final int pageNumber;
    private final int pageSize;

    private CustomPageable(
        final int pageNumber,
        final int pageSize
    ) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public static CustomPageable createPage(
        final int page,
        final int limit
    ) {
        return new CustomPageable(page, limit);
    }

    public int getOffset() {
        return (pageNumber - 1) * pageSize;
    }

    public int getTotalPageCount(final int totalCount) {
        return (totalCount - 1) / pageSize + 1;
    }

    @Override
    public String toString() {
        return String.format("{\"page\": \"%s\", \"limit\": \"%s\"}", pageNumber, pageSize);
    }
}
