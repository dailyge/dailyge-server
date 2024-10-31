package project.dailyge.app.core.event.persistence;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.event.EventEntityReadRepository;
import project.dailyge.entity.event.EventJpaEntity;

@Repository
public class EventEntityReadDao implements EventEntityReadRepository {

    private final EntityManager entityManager;

    public EventEntityReadDao(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public EventJpaEntity findById(final Long eventId) {
        return entityManager.find(EventJpaEntity.class, eventId);
    }
}
