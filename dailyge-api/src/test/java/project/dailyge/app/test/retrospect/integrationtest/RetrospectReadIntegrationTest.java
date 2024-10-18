package project.dailyge.app.test.retrospect.integrationtest;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import project.dailyge.app.common.DatabaseTestBase;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.core.retrospect.application.RetrospectReadService;
import project.dailyge.app.core.retrospect.application.RetrospectWriteService;
import project.dailyge.app.core.retrospect.application.command.RetrospectCreateCommand;
import project.dailyge.app.core.retrospect.persistence.RetrospectEntityReadDao;
import project.dailyge.app.paging.CustomPageable;
import project.dailyge.app.response.AsyncPagingResponse;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.DATA_ACCESS_EXCEPTION;

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
        final AsyncPagingResponse<RetrospectJpaEntity> asyncPagingResponse = retrospectReadService.findRetrospectAndTotalCountByPage(dailygeUser,
            page);
        final List<RetrospectJpaEntity> findRetrospects = asyncPagingResponse.data();
        final int totalCount = asyncPagingResponse.totalCount();

        assertEquals(10, findRetrospects.size());
        assertEquals(10, totalCount);
    }

    @Test
    @DisplayName("회고 조회 시 페이징을 최신 데이터 순으로 반환한다.")
    void whenFindRetrospectThenResultShouldBeRecentDatePagingReturned() {
        final CustomPageable page = CustomPageable.createPage(3, 1);
        final AsyncPagingResponse<RetrospectJpaEntity> asyncPagingResponse = retrospectReadService.findRetrospectAndTotalCountByPage(dailygeUser,
            page);

        final List<RetrospectJpaEntity> findRetrospects = asyncPagingResponse.data();
        assertEquals(1, findRetrospects.size());

        final RetrospectJpaEntity findRetrospect = findRetrospects.get(0);
        assertEquals(8, findRetrospect.getId());
    }

    @Test
    @DisplayName("페이지 이후의 데이터가 없다면, 빈 리스트을 반환한다.")
    void whenNoDataAfterThePageThenResultShouldBeEmptyList() {
        final CustomPageable page = CustomPageable.createPage(2, 10);
        final AsyncPagingResponse<RetrospectJpaEntity> asyncPagingResponse = retrospectReadService.findRetrospectAndTotalCountByPage(dailygeUser,
            page);
        final List<RetrospectJpaEntity> findRetrospects = asyncPagingResponse.data();

        assertTrue(findRetrospects.isEmpty());
    }

    @Test
    @DisplayName("자신의 회고 전체 개수만 반환한다.")
    void whenFindTotalCountThenResultShouldBeMyTotalRetrospectCount() {
        final CustomPageable page = CustomPageable.createPage(1, 10);
        final RetrospectCreateCommand createCommand = new RetrospectCreateCommand("회고 제목", "회고 내용", now.atTime(0, 0, 0, 0), false);
        retrospectWriteService.save(invalidUser, createCommand);

        final AsyncPagingResponse<RetrospectJpaEntity> asyncPagingResponse = retrospectReadService.findRetrospectAndTotalCountByPage(dailygeUser,
            page);
        final int totalCount = asyncPagingResponse.totalCount();

        assertEquals(10, totalCount);
    }

    @Test
    @DisplayName("비동기 조회 시 JdbcTemplate 장애가 발생하면, CommonException이 발생한다.")
    void whenJdbcTemplateFailedInAsyncPagingThenCommonExceptionShouldBeHappen() {
        final CustomPageable page = CustomPageable.createPage(1, 10);
        final JdbcTemplate mockJdbcTemplate = mock(JdbcTemplate.class);
        final JPAQueryFactory mockJPAQueryFactory = mock(JPAQueryFactory.class);
        final RetrospectEntityReadDao retrospectEntityReadDao = new RetrospectEntityReadDao(mockJPAQueryFactory, mockJdbcTemplate);

        when(mockJdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyLong()))
            .thenThrow(new DataAccessException("Query execution exception") {
            });

        assertThatThrownBy(() -> retrospectEntityReadDao.findRetrospectAndTotalCountByPage(dailygeUser.getUserId(), page))
            .isInstanceOf(CompletionException.class)
            .hasCauseInstanceOf(CommonException.class)
            .hasMessageContaining(DATA_ACCESS_EXCEPTION.message());
    }

    @Test
    @DisplayName("비동기 조회 시 JPAQueryFactory 장애가 발생하면, CommonException이 발생한다.")
    void whenJPAQueryFactoryFailedInAsyncPagingThenCommonExceptionShouldBeHappen() {
        final CustomPageable page = CustomPageable.createPage(1, 10);
        final JdbcTemplate mockJdbcTemplate = mock(JdbcTemplate.class);
        final JPAQueryFactory mockJPAQueryFactory = mock(JPAQueryFactory.class);
        final RetrospectEntityReadDao retrospectEntityReadDao = new RetrospectEntityReadDao(mockJPAQueryFactory, mockJdbcTemplate);

        when(mockJPAQueryFactory.selectFrom(any()))
            .thenThrow(new DataAccessException("Query execution exception") {
            });

        assertThatThrownBy(() -> retrospectEntityReadDao.findRetrospectAndTotalCountByPage(dailygeUser.getUserId(), page))
            .isInstanceOf(CompletionException.class)
            .hasCauseInstanceOf(CommonException.class)
            .hasMessageContaining(DATA_ACCESS_EXCEPTION.message());
    }
}
