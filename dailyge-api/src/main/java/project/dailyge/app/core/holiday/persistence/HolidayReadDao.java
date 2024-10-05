package project.dailyge.app.core.holiday.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.holiday.HolidayEntityReadRepository;
import project.dailyge.entity.holiday.HolidayJpaEntity;
import static project.dailyge.entity.holiday.QHolidayJpaEntity.holidayJpaEntity;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
class HolidayReadDao implements HolidayEntityReadRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<HolidayJpaEntity> findHolidaysByDate(
        final Long countryId,
        final LocalDate startDate,
        final LocalDate endDate
    ) {
        return queryFactory.selectFrom(holidayJpaEntity)
            .where(holidayJpaEntity.countryId.eq(countryId)
                .and(holidayJpaEntity.date.between(startDate, endDate))
            ).fetch();
    }
}
