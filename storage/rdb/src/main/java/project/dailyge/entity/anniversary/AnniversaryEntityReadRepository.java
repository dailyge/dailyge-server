package project.dailyge.entity.anniversary;

import java.util.Optional;

public interface AnniversaryEntityReadRepository {
    Optional<AnniversaryJpaEntity> findById(Long anniversaryId);
}
