package project.dailyge.entity.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import project.dailyge.entity.BaseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    @Column(name = "user_id")
    private Long userId;

    protected TaskRecurrenceJpaEntity() {
    }

    public TaskRecurrenceJpaEntity(
        final Long id,
        final RecurrenceType recurrenceType,
        final List<Integer> datePattern,
        final String title,
        final String content,
        final LocalDateTime startDate,
        final LocalDateTime endDate,
        final Long userId
    ) {
        this.id = id;
        this.recurrenceType = recurrenceType;
        this.datePattern = datePattern;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
    }

    public TaskRecurrenceJpaEntity(
        final RecurrenceType recurrenceType,
        final List<Integer> datePattern,
        final String title,
        final String content,
        final LocalDateTime startDate,
        final LocalDateTime endDate,
        final Long userId
    ) {
        this.recurrenceType = recurrenceType;
        this.datePattern = datePattern;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public RecurrenceType getRecurrenceType() {
        return recurrenceType;
    }

    public List<Integer> getDatePattern() {
        return datePattern;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public String getStartDateAsString() {
        return startDate.toString();
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public String getEndDateAsString() {
        return endDate.toString();
    }

    public Long getUserId() {
        return userId;
    }

    public void update(
        final String title,
        final String content
    ) {
        this.title = title;
        this.content = content;
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

    public void delete() {
        updateDeletedStatus();
    }
}
