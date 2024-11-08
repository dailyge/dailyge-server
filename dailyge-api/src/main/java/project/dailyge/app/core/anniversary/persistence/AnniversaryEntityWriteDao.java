package project.dailyge.app.core.anniversary.persistence;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.anniversary.AnniversaryEntityWriteRepository;
import project.dailyge.entity.anniversary.AnniversaryJpaEntity;

@Repository
class AnniversaryEntityWriteDao implements AnniversaryEntityWriteRepository {

    private final EntityManager entityManager;

    public AnniversaryEntityWriteDao(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Long save(final AnniversaryJpaEntity anniversary) {
        entityManager.persist(anniversary);
        return anniversary.getId();
    }
}
