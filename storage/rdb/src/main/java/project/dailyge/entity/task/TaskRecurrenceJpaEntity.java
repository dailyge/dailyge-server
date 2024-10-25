package project.dailyge.entity.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import project.dailyge.entity.BaseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity(name = "task_recurrences")
public class TaskRecurrenceJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence_type")
    private RecurrenceType recurrenceType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "date_pattern", columnDefinition = "json")
    private List<Integer> datePattern;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    public TaskRecurrenceJpaEntity(
        final Long id,
        final RecurrenceType recurrenceType,
        final List<Integer> datePattern,
        final String title,
        final String content,
        final LocalDateTime startDate,
        final LocalDateTime endDate
    ) {
        this.id = id;
        this.recurrenceType = recurrenceType;
        this.datePattern = datePattern;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof TaskRecurrenceJpaEntity that)) {
            return false;
        }
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
