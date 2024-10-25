package project.dailyge.app.core.retrospect.persistence;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.retrospect.RetrospectEntityWriteRepository;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;

@Repository
@RequiredArgsConstructor
class RetrospectWriteDao implements RetrospectEntityWriteRepository {

    private final EntityManager entityManager;

    @Override
    public Long save(final RetrospectJpaEntity retrospect) {
        entityManager.persist(retrospect);
        return retrospect.getId();
    }
}
