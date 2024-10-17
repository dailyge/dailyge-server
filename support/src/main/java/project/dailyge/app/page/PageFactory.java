package project.dailyge.app.page;

public final class PageFactory {

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MIN_PAGE_SIZE = 5;
    private static final int MAX_PAGE_SIZE = 30;

    private PageFactory() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static CustomPageable createPage(
        final String pageNumber,
        final String pageSize
    ) {
        return CustomPageable.createPage(
            getPageNumber(pageNumber),
            getPageSize(pageSize)
        );
    }

    private static int getPageNumber(final String pageNumber) {
        final int parsedPageNumber = parsePageNumber(pageNumber);
        if (parsedPageNumber < DEFAULT_PAGE_NUMBER) {
            return DEFAULT_PAGE_NUMBER;
        }
        return parsedPageNumber;
    }

    private static int parsePageNumber(final String pageNumber) {
        if (pageNumber == null) {
            return DEFAULT_PAGE_NUMBER;
        }
        try {
            return Integer.parseInt(pageNumber);
        } catch (NumberFormatException exception) {
            return DEFAULT_PAGE_NUMBER;
        }
    }

    private static int getPageSize(final String pageSize) {
        final int parsedPageSize = parsePageSize(pageSize);
        if (parsedPageSize < MIN_PAGE_SIZE || parsedPageSize > MAX_PAGE_SIZE) {
            return DEFAULT_PAGE_SIZE;
        }
        return parsedPageSize;
    }

    private static int parsePageSize(final String pageSize) {
        if (pageSize == null) {
            return DEFAULT_PAGE_SIZE;
        }
        try {
            return Integer.parseInt(pageSize);
        } catch (NumberFormatException exception) {
            return DEFAULT_PAGE_SIZE;
        }
    }
}
