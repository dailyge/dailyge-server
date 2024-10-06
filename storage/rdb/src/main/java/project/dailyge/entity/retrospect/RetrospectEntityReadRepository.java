package project.dailyge.entity.retrospect;

import java.util.Optional;

public interface RetrospectEntityReadRepository {
    Optional<RetrospectJpaEntity> findById(Long retrospectId);
}
