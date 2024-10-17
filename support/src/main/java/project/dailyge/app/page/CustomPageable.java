package project.dailyge.app.page;

import lombok.Getter;

@Getter
public class CustomPageable {

    private final Integer pageNumber;
    private final Integer pageSize;

    private CustomPageable(
        final Integer pageNumber,
        final Integer pageSize
    ) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public static CustomPageable createPage(
        final Integer page,
        final Integer limit
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
