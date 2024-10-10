package project.dailyge.app.page;

public final class PageFactory {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_LIMIT = 10;
    private static final int MAX_LIMIT = 30;

    private PageFactory() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static Page createPage(
        final String page,
        final String limit
    ) {
        return Page.createPage(
            getPage(page),
            getLimit(limit)
        );
    }

    private static Integer getPage(final String page) {
        final Integer parsedPage = parsePage(page);
        if (parsedPage < DEFAULT_PAGE) {
            return DEFAULT_PAGE;
        }
        return parsedPage;
    }

    private static Integer parsePage(final String page) {
        if (page == null) {
            return DEFAULT_PAGE;
        }
        try {
            return Integer.parseInt(page);
        } catch (NumberFormatException exception) {
            return DEFAULT_PAGE;
        }
    }

    private static Integer getLimit(final String limit) {
        final Integer pageSizeLimit = parseLimit(limit);
        if (pageSizeLimit < 1 || pageSizeLimit > MAX_LIMIT) {
            return DEFAULT_LIMIT;
        }
        return pageSizeLimit;
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
