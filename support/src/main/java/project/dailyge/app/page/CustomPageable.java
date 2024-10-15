package project.dailyge.app.page;

import lombok.Getter;

@Getter
public class CustomPageable {

    private final Integer page;
    private final Integer limit;

    private CustomPageable(
        final Integer page,
        final Integer limit
    ) {
        this.page = page;
        this.limit = limit;
    }

    public static CustomPageable createPage(
        final Integer page,
        final Integer limit
    ) {
        return new CustomPageable(page, limit);
    }

    public int getOffset() {
        return (page - 1) * limit;
    }

    public int getTotalPageCount(final int totalCount) {
        return (totalCount - 1) / limit + 1;
    }

    @Override
    public String toString() {
        return String.format("{\"page\": \"%s\", \"limit\": \"%s\"}", page, limit);
    }
}
