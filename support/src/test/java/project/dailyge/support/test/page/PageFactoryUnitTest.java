package project.dailyge.support.test.page;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.paging.CustomPageable;
import project.dailyge.app.paging.PageFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("[UnitTest] PageFactory 단위 테스트")
class PageFactoryUnitTest {

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Test
    @DisplayName("유효한 page와 pageSize로 Page를 생성하면, 올바르게 생성된다.")
    void whenCreatePageWithValidArgumentThenCorrectlyCreatedPage() {
        final String pageNumber = "10";
        final String pageSize = "20";
        final CustomPageable newPage = PageFactory.createPage(pageNumber, pageSize);

        assertNotNull(newPage);
        assertEquals(Integer.parseInt(pageNumber), newPage.getPageNumber());
        assertEquals(Integer.parseInt(pageSize), newPage.getPageSize());
    }

    @Test
    @DisplayName("유효하지 않는 page와 pageSize이면, 기본 설정값으로 생성된다.")
    void whenCreatePageWithInvalidArgumentThenDefaultSettingsUsed() {
        final String pageNumber = "-1";
        final String pageSize = "-1";
        final CustomPageable newPage = PageFactory.createPage(pageNumber, pageSize);

        assertNotNull(newPage);
        assertEquals(DEFAULT_PAGE_NUMBER, newPage.getPageNumber());
        assertEquals(DEFAULT_PAGE_SIZE, newPage.getPageSize());
    }

    @Test
    @DisplayName("page와 pageSize가 null이면, 기본 설정값으로 생성된다.")
    void whenPageAndpageSizeIsNullThenDefaultSettingsUsed() {
        final CustomPageable newPage = PageFactory.createPage(null, null);

        assertNotNull(newPage);
        assertEquals(DEFAULT_PAGE_NUMBER, newPage.getPageNumber());
        assertEquals(DEFAULT_PAGE_SIZE, newPage.getPageSize());
    }

    @Test
    @DisplayName("pageSize가 최대 pageSize 보다 크면, 기본 pageSize값으로 설정된다.")
    void whenpageSizeGreaterMaximumpageSizeThenDefaultpageSizeUsed() {
        final String pageNumber = "5";
        final String pageSize = "500";
        final CustomPageable newPage = PageFactory.createPage(pageNumber, pageSize);

        assertNotNull(newPage);
        assertEquals(Integer.parseInt(pageNumber), newPage.getPageNumber());
        assertEquals(DEFAULT_PAGE_SIZE, newPage.getPageSize());
    }
}
