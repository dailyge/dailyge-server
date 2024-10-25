package project.dailyge.app.core.holiday.persistence;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.holiday.HolidayEntityWriteRepository;
import project.dailyge.entity.holiday.HolidayJpaEntity;

@Repository
@RequiredArgsConstructor
class HolidayWriteDao implements HolidayEntityWriteRepository {

    private final EntityManager entityManager;

    @Override
    public Long save(final HolidayJpaEntity holiday) {
        entityManager.persist(holiday);
        return holiday.getId();
    }
}
