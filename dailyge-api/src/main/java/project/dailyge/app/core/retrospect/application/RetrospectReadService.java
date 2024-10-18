package project.dailyge.app.core.retrospect.application;

import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.paging.CustomPageable;
import project.dailyge.app.response.AsyncPagingResponse;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;

public interface RetrospectReadService {
    RetrospectJpaEntity findById(Long retrospectId);

    AsyncPagingResponse<RetrospectJpaEntity> findRetrospectAndTotalCountByPage(DailygeUser dailygeUser, CustomPageable page);
}
