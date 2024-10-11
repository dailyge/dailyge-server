package project.dailyge.support.test.page;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.app.page.CustomPageable;
import project.dailyge.app.page.PageFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("[UnitTest] PageFactory 단위 테스트")
class PageFactoryUnitTest {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_LIMIT = 10;

    @Test
    @DisplayName("유효한 page와 limit로 Page를 생성하면, 올바르게 생성된다.")
    void whenCreatePageWithValidArgumentThenCorrectlyCreatedPage() {
        final String page = "10";
        final String limit = "20";
        final CustomPageable newPage = PageFactory.createPage(page, limit);

        assertNotNull(newPage);
        assertEquals(Integer.parseInt(page), newPage.getPage());
        assertEquals(Integer.parseInt(limit), newPage.getLimit());
    }

    @Test
    @DisplayName("유효하지 않는 page와 limit이면, 기본 설정값으로 생성된다.")
    void whenCreatePageWithInvalidArgumentThenDefaultSettingsUsed() {
        final String page = "-1";
        final String limit = "-1";
        final CustomPageable newPage = PageFactory.createPage(page, limit);

        assertNotNull(newPage);
        assertEquals(DEFAULT_PAGE, newPage.getPage());
        assertEquals(DEFAULT_LIMIT, newPage.getLimit());
    }

    @Test
    @DisplayName("page와 limit가 null이면, 기본 설정값으로 생성된다.")
    void whenPageAndLimitIsNullThenDefaultSettingsUsed() {
        final CustomPageable newPage = PageFactory.createPage(null, null);

        assertNotNull(newPage);
        assertEquals(DEFAULT_PAGE, newPage.getPage());
        assertEquals(DEFAULT_LIMIT, newPage.getLimit());
    }

    @Test
    @DisplayName("limit가 최대 limit 보다 크면, 기본 limit값으로 설정된다.")
    void whenLimitGreaterMaximumLimitThenDefaultLimitUsed() {
        final String page = "5";
        final String limit = "500";
        final CustomPageable newPage = PageFactory.createPage(page, limit);

        assertNotNull(newPage);
        assertEquals(Integer.parseInt(page), newPage.getPage());
        assertEquals(DEFAULT_LIMIT, newPage.getLimit());
    }
}
