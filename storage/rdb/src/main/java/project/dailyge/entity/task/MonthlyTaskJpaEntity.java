package project.dailyge.entity.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.dailyge.entity.BaseEntity;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "monthly_tasks")
public class MonthlyTaskJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year")
    private int year;

    @Column(name = "month")
    private int month;

    @Column(name = "user_id")
    private Long userId;

    public MonthlyTaskJpaEntity(
        final Long id,
        final int year,
        final int month,
        final Long userId
    ) {
        validate(year, month, userId);
        this.id = id;
        this.year = year;
        this.month = month;
        this.userId = userId;
    }

    private void validate(
        final int year,
        final int month,
        final Long userId
    ) {
        if (year < 1) {
            throw new IllegalArgumentException("올바른 년도를 입력해주세요.");
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("올바른 년도를 입력해주세요.");
        }
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID를 입력해주세요.");
        }
    }

    public MonthlyTaskJpaEntity(
        final int year,
        final int month,
        final Long userId
    ) {
        this(null, year, month, userId);
    }
}
