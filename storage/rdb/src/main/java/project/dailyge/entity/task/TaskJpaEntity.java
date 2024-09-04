package project.dailyge.entity.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.dailyge.entity.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity(name = "tasks")
public class TaskJpaEntity extends BaseEntity {

    private static final int MAX_TITLE_LENGTH = 150;
    private static final int MAX_CONTENT_LENGTH = 2_500;
    private static final int ONE_YEAR = 12;

    private static final String OVER_MAX_TITLE_LENGTH_ERROR_MESSAGE = "입력 가능한 최대 제목 길이를 초과했습니다.";
    private static final String OVER_MAX_CONTENT_LENGTH_ERROR_MESSAGE = "입력 가능한 최대 내용 길이를 초과했습니다.";
    private static final String DATE_BETWEEN_BEFORE_OR_AFTER_ERROR_MESSAGE = "5년 이내의 일정을 등록해주세요.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "year")
    private int year;

    @Column(name = "month")
    private int month;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "monthly_task_id")
    private Long monthlyTaskId;

    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    private TaskColor color;

    public TaskJpaEntity(
        final String title,
        final String content,
        final LocalDate date,
        final TaskStatus status,
        final Long userId
    ) {
        this(title, content, date, status, null, null, userId);
    }

    public TaskJpaEntity(
        final String title,
        final String content,
        final LocalDate date,
        final TaskStatus status,
        final TaskColor color,
        final Long monthlyTaskId,
        final Long userId
    ) {
        validate(title, content, date);
        this.title = title;
        this.content = content;
        this.status = status;
        this.date = date;
        this.year = date.getYear();
        this.month = date.getMonthValue();
        this.color = color;
        this.monthlyTaskId = monthlyTaskId;
        this.userId = userId;
        this.createdBy = userId;
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    public TaskJpaEntity(
        final Long id,
        final String title,
        final String content,
        final LocalDate date,
        final TaskStatus status,
        final Long monthlyTaskId,
        final Long userId,
        final LocalDateTime createdAt,
        final Long createdBy,
        final LocalDateTime lastModifiedAt,
        final Long lastModifiedBy,
        final boolean deleted
    ) {
        validate(title, content, date);
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.year = date.getYear();
        this.month = date.getMonthValue();
        this.status = status;
        this.monthlyTaskId = monthlyTaskId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.lastModifiedAt = lastModifiedAt;
        this.lastModifiedBy = lastModifiedBy;
        this.deleted = deleted;
    }

    private void validate(
        final String title,
        final String content,
        final LocalDate date
    ) {
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException(OVER_MAX_TITLE_LENGTH_ERROR_MESSAGE);
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException(OVER_MAX_CONTENT_LENGTH_ERROR_MESSAGE);
        }

        final LocalDate today = LocalDate.now();
        final LocalDate dateBeforeFiveYears = today.minusYears(5);
        final LocalDate dateAfterFiveYears = today.plusYears(5);
        if (date.isBefore(dateBeforeFiveYears) || date.isAfter(dateAfterFiveYears)) {
            throw new IllegalArgumentException(DATE_BETWEEN_BEFORE_OR_AFTER_ERROR_MESSAGE);
        }
    }

    public String getOverMaxTitleLengthErrorMessage() {
        return OVER_MAX_TITLE_LENGTH_ERROR_MESSAGE;
    }

    public String getOverMaxContentLengthErrorMessage() {
        return OVER_MAX_CONTENT_LENGTH_ERROR_MESSAGE;
    }

    public String getDateErrorMessage() {
        return DATE_BETWEEN_BEFORE_OR_AFTER_ERROR_MESSAGE;
    }

    public boolean isOwner(Long userId) {
        return this.userId.equals(userId);
    }

    public int getDay() {
        return date.getDayOfMonth();
    }

    public int getWeekOfMonth() {
        return date.getDayOfWeek().getValue();
    }

    public void update(
        final String title,
        final String content,
        final LocalDate date,
        final TaskStatus status,
        final Long monthlyTaskId,
        final TaskColor color
    ) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.year = date.getYear();
        this.month = date.getMonthValue();
        this.status = status;
        this.monthlyTaskId = monthlyTaskId;
        this.color = color;
    }

    public void updateStatus(final TaskStatus status) {
        this.status = status;
    }

    public void delete() {
        this.deleted = true;
    }

    public boolean isValidMonthlyTask(final Long monthlyTaskId) {
        return getId().equals(monthlyTaskId);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final TaskJpaEntity task = (TaskJpaEntity) obj;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
