package project.dailyge.app.core.anniversary.application;

import project.dailyge.entity.anniversary.AnniversaryJpaEntity;

public interface AnniversaryReadService {
    AnniversaryJpaEntity findById(Long anniversaryId);
}
