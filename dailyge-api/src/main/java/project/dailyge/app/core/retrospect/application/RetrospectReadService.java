package project.dailyge.app.core.retrospect.application;

import project.dailyge.entity.retrospect.RetrospectJpaEntity;

public interface RetrospectReadService {
    RetrospectJpaEntity findById(Long retrospectId);
}
