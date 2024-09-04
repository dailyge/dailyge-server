package project.dailyge.entity.monthlygoal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.dailyge.entity.BaseEntity;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity(name = "monthly_goals")
public class MonthlyGoalJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "done")
    private boolean done;

    @Column(name = "year")
    private int year;

    @Column(name = "month")
    private int month;

    @Column(name = "user_id")
    private Long userId;

    public MonthlyGoalJpaEntity(
        final Long id,
        final String title,
        final String content,
        final boolean done,
        final LocalDate date,
        final Long userId
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.done = done;
        this.year = date.getYear();
        this.month = date.getMonthValue();
        this.userId = userId;
    }

    public MonthlyGoalJpaEntity(
        final String title,
        final String content,
        final LocalDate date,
        final Long userId
    ) {
        this(null, title, content, false, date, userId);
    }

    public void update(
        final String title,
        final String content
    ) {
        this.title = title;
        this.content = content;
    }

    public void updateStatus(final Boolean done) {
        this.done = done;
    }

    public void delete() {
        deleted = true;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MonthlyGoalJpaEntity that = (MonthlyGoalJpaEntity) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
