package project.dailyge.app.core.event.persistence;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.entity.event.EventEntityWriteRepository;
import project.dailyge.entity.event.EventJpaEntity;

@Repository
public class EventEntityWriteDao implements EventEntityWriteRepository {

    private final EntityManager entityManager;

    public EventEntityWriteDao(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Long save(final EventJpaEntity event) {
        entityManager.persist(event);
        return event.getId();
    }
}
