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
import project.dailyge.app.page.CustomPageable;
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
        for (int i = 0; i < 10; i++) {
            final RetrospectCreateCommand createCommand = new RetrospectCreateCommand("회고 제목", "회고 내용", now.atTime(i, 0, 0, 0), false);
            retrospectWriteService.save(dailygeUser, createCommand);
        }
    }

    @Test
    @DisplayName("회고 조회 시 정상적으로 반환한다.")
    void whenFindRetrospectThenResultShouldBeCorrectly() {
        final CustomPageable page = CustomPageable.createPage(1, 10);
        final List<RetrospectJpaEntity> findRetrospects = retrospectReadService.findRetrospectByPage(dailygeUser, page);

        assertEquals(10, findRetrospects.size());
    }

    @Test
    @DisplayName("회고 조회 시 페이징을 최신 데이터 순으로 반환한다.")
    void whenFindRetrospectThenResultShouldBeRecentDatePagingReturned() {
        final CustomPageable page = CustomPageable.createPage(3, 1);
        final List<RetrospectJpaEntity> findRetrospects = retrospectReadService.findRetrospectByPage(dailygeUser, page);

        assertEquals(1, findRetrospects.size());

        final RetrospectJpaEntity findRetrospect = findRetrospects.get(0);
        assertEquals(8, findRetrospect.getId());
    }


    @Test
    @DisplayName("페이지 이후의 데이터가 없다면, 빈 리스트을 반환한다.")
    void whenNoDataAfterThePageThenResultShouldBeEmptyList() {
        final CustomPageable page = CustomPageable.createPage(2, 10);
        final List<RetrospectJpaEntity> findRetrospects = retrospectReadService.findRetrospectByPage(dailygeUser, page);

        assertTrue(findRetrospects.isEmpty());
    }


    @Test
    @DisplayName("자신의 회고 전체 개수만 반환한다.")
    void whenFindTotalCountThenResultShouldBeMyTotalRetrospectCount() {
        final RetrospectCreateCommand createCommand = new RetrospectCreateCommand("회고 제목", "회고 내용", now.atTime(0, 0, 0, 0), false);
        retrospectWriteService.save(invalidUser, createCommand);

        final int totalCount = retrospectReadService.findTotalCount(dailygeUser);

        assertEquals(10, totalCount);
    }
}
