package project.dailyge.app.test.retrospect.integrationtest;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.core.retrospect.application.RetrospectReadService;
import project.dailyge.app.core.retrospect.application.RetrospectWriteService;
import project.dailyge.app.core.retrospect.application.command.RetrospectCreateCommand;
import project.dailyge.app.page.Page;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[IntegrationTest] 회고 조회 통합 테스트")
class RetrospectReadIntegrationTest extends DatabaseTestBase {

    @Autowired
    private RetrospectReadService retrospectReadService;

    @Autowired
    private RetrospectWriteService retrospectWriteService;

    @BeforeEach
    void setUp() {
        now = LocalDate.now();
        final RetrospectCreateCommand createCommand = new RetrospectCreateCommand("회고 제목", "회고 내용", now.atTime(0, 0, 0, 0), false);
        for (int i = 0; i < 10; i++) {
            retrospectWriteService.save(dailygeUser, createCommand);
        }
    }

    @Test
    @DisplayName("회고 조회 시 정상적으로 반환한다.")
    void whenFindRetrospectThenResultShouldBeCorrectly() {
        final Page page = Page.createPage(1, 10);
        final List<RetrospectJpaEntity> findRetrospects = retrospectReadService.findRetrospectByPage(dailygeUser, page);

        assertEquals(10, findRetrospects.size());
    }

    @Test
    @DisplayName("회고 조회 시 페이징에 맞는 데이터를 반환한다.")
    void whenFindRetrospectThenResultShouldBePagingCorrectly() {
        final Page page = Page.createPage(3, 1);
        final List<RetrospectJpaEntity> findRetrospects = retrospectReadService.findRetrospectByPage(dailygeUser, page);

        assertEquals(1, findRetrospects.size());

        final RetrospectJpaEntity findRetrospect = findRetrospects.get(0);
        assertEquals(3, findRetrospect.getId());
    }


    @Test
    @DisplayName("페이지 이후의 데이터가 없다면, 빈 리스트을 반환한다.")
    void whenNoDataAfterThePageThenResultShouldBeEmptyList() {
        final Page page = Page.createPage(2, 10);
        final List<RetrospectJpaEntity> findRetrospects = retrospectReadService.findRetrospectByPage(dailygeUser, page);

        assertTrue(findRetrospects.isEmpty());
    }
}
