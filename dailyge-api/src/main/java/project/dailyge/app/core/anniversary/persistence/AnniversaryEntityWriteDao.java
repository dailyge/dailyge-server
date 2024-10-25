package project.dailyge.app.core.anniversary.persistence;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.anniversary.AnniversaryJpaEntity;
import project.dailyge.entity.anniversary.AnniversaryEntityWriteRepository;

@Repository
@RequiredArgsConstructor
class AnniversaryEntityWriteDao implements AnniversaryEntityWriteRepository {

    private final EntityManager entityManager;

    @Override
    public Long save(final AnniversaryJpaEntity anniversary) {
        entityManager.persist(anniversary);
        return anniversary.getId();
    }
}
