package project.dailyge.support.test.page;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import project.dailyge.app.page.CustomPageable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("[UnitTest] Page 단위 테스트")
class CustomPageableUnitTest {

    private static final int PAGE_NUMBER = 1;
    private static final int PAGE_SIZE = 10;
    private CustomPageable page;

    @BeforeEach
    void setUp() {
        page = CustomPageable.createPage(PAGE_NUMBER, PAGE_SIZE);
    }

    @Test
    @DisplayName("Page 생성 시 올바르게 초기화 된다.")
    void whenCreatePageThenInitializeCorrectly() {
        assertNotNull(page);
        assertEquals(PAGE_NUMBER, page.getPageNumber());
        assertEquals(PAGE_SIZE, page.getPageSize());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 10, 100})
    @DisplayName("Page를 통한 offset이 정확하게 계산이 된다.")
    void whenCalculatePageOffsetThenResultShouldBeCorrectly(final int pageNumber) {
        final CustomPageable newPage = CustomPageable.createPage(pageNumber, PAGE_SIZE);

        assertEquals((pageNumber - 1) * PAGE_SIZE, newPage.getOffset());
    }

    @Test
    @DisplayName("limit에 따른 전체 페이지 개수를 올림하여 계산한다.")
    void whenCalculateTotalPageCountThenResultShouldBeCorrectly() {
        final CustomPageable newPage = CustomPageable.createPage(PAGE_NUMBER, PAGE_SIZE);

        assertEquals(4, newPage.getTotalPageCount(33));
    }

    @Test
    @DisplayName("Page 객체의 toString 메서드는 올바른 JSON 형식의 문자열을 반환한다.")
    void whenToStringCalledThenReturnJsonFormatString() {
        assertEquals("{\"page\": \"1\", \"limit\": \"10\"}", page.toString());
    }
}
