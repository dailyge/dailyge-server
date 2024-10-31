package project.dailyge.app.core.retrospect.persistence;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.retrospect.RetrospectEntityWriteRepository;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;

@Repository
class RetrospectEntityWriteDao implements RetrospectEntityWriteRepository {

    private final EntityManager entityManager;

    public RetrospectEntityWriteDao(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Long save(final RetrospectJpaEntity retrospect) {
        entityManager.persist(retrospect);
        return retrospect.getId();
    }
}
