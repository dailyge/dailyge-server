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

    private static final int PAGE = 1;
    private static final int LIMIT = 10;
    private CustomPageable page;

    @BeforeEach
    void setUp() {
        page = CustomPageable.createPage(PAGE, LIMIT);
    }

    @Test
    @DisplayName("Page 생성 시 올바르게 초기화 된다.")
    void whenCreatePageThenInitializeCorrectly() {
        assertNotNull(page);
        assertEquals(PAGE, page.getPage());
        assertEquals(LIMIT, page.getLimit());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 10, 100})
    @DisplayName("Page를 통한 offset이 정확하게 계산이 된다.")
    void whenCalculatePageOffsetThenResultShouldBeCorrectly(final int page) {
        final CustomPageable newPage = CustomPageable.createPage(page, LIMIT);

        assertEquals((page - 1) * LIMIT, newPage.getOffset());
    }

    @Test
    @DisplayName("Page 객체의 toString 메서드는 올바른 JSON 형식의 문자열을 반환한다.")
    void whenToStringCalledThenReturnJsonFormatString() {
        assertEquals("{\"page\": \"1\", \"limit\": \"10\"}", page.toString());
    }
}
