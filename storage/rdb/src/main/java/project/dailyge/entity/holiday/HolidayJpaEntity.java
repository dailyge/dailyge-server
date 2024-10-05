package project.dailyge.entity.holiday;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.dailyge.entity.BaseEntity;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity(name = "holidays")
public class HolidayJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "holiday")
    private boolean holiday;

    @Column(name = "country_id")
    private Long countryId;

    public HolidayJpaEntity(
        final Long id,
        final String name,
        final LocalDate date,
        final boolean holiday,
        final Long countryId
    ) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.holiday = holiday;
        this.countryId = countryId;
    }

    public String getDateAsString() {
        return date.toString();
    }
}
