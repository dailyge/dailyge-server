package project.dailyge.app.core.holiday.persistence;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.holiday.HolidayEntityWriteRepository;
import project.dailyge.entity.holiday.HolidayJpaEntity;

@Repository
class HolidayWriteDao implements HolidayEntityWriteRepository {

    private final EntityManager entityManager;

    public HolidayWriteDao(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Long save(final HolidayJpaEntity holiday) {
        entityManager.persist(holiday);
        return holiday.getId();
    }
}
