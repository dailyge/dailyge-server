package project.dailyge.app.core.retrospect.application;

import java.util.List;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.page.Page;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;

public interface RetrospectReadService {
    RetrospectJpaEntity findById(Long retrospectId);

    List<RetrospectJpaEntity> findRetrospectByPage(DailygeUser dailygeUser, Page page);
}
