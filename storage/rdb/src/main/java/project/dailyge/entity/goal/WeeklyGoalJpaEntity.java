package project.dailyge.entity.goal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import project.dailyge.entity.BaseEntity;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "weekly_goals")
public class WeeklyGoalJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "done")
    private boolean done;

    @Column(name = "week_start_date")
    private LocalDateTime weekStartDate;

    @Column(name = "user_id")
    private Long userId;

    public WeeklyGoalJpaEntity() {
    }

    public WeeklyGoalJpaEntity(
        final Long id,
        final String title,
        final String content,
        final boolean done,
        final LocalDateTime weekStartDate,
        final Long userId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.done = done;
        this.weekStartDate = weekStartDate;
        this.userId = userId;
    }

    public WeeklyGoalJpaEntity(
        final Long id,
        final String title,
        final String content,
        final LocalDateTime dateTime,
        final Long userId
    ) {
        this(id, title, content, false, dateTime.with(DayOfWeek.MONDAY), userId);
    }

    public WeeklyGoalJpaEntity(
        final String title,
        final String content,
        final LocalDateTime dateTime,
        final Long userId
    ) {
        this(null, title, content, dateTime, userId);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean isDone() {
        return done;
    }

    public LocalDateTime getWeekStartDate() {
        return weekStartDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void update(
        final String updatedTitle,
        final String updatedContent
    ) {
        this.title = updatedTitle;
        this.content = updatedContent;
    }

    public void delete() {
        updateDeletedStatus();
    }

    public void updateDone(final Boolean done) {
        this.done = done;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof WeeklyGoalJpaEntity that)) {
            return false;
        }
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
