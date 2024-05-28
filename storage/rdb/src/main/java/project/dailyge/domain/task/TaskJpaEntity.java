package project.dailyge.domain.task;

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
import project.dailyge.domain.BaseEntity;

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
    private static final String PAST_DATE_ERROR_MESSAGE = "과거 날짜는 등록할 수 없습니다.";
    private static final String BEYOND_ONE_YEAR_ERROR_MESSAGE = "1년 이내의 일정을 등록해주세요.";

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

    public TaskJpaEntity(
        final String title,
        final String content,
        final LocalDate date,
        final TaskStatus status,
        final Long userId
    ) {
        validate(title, content, date);
        this.title = title;
        this.content = content;
        this.status = status;
        this.date = date;
        this.year = date.getYear();
        this.month = date.getMonthValue();
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
        final Long userId,
        final LocalDateTime createdAt,
        final Long createdBy,
        final LocalDateTime lastModifiedAt,
        final Long lastModifiedBy,
        final Boolean deleted
    ) {
        validate(title, content, date);
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.year = date.getYear();
        this.month = date.getMonthValue();
        this.status = status;
        this.userId = userId;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.lastModifiedAt = lastModifiedAt;
        this.lastModifiedBy = lastModifiedBy;
        this.deleted = deleted;
    }

    private void validate(
        String title,
        String content,
        LocalDate date
    ) {
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException(OVER_MAX_TITLE_LENGTH_ERROR_MESSAGE);
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException(OVER_MAX_CONTENT_LENGTH_ERROR_MESSAGE);
        }

        LocalDate today = LocalDate.now();
        if (date.isBefore(today)) {
            throw new IllegalArgumentException(PAST_DATE_ERROR_MESSAGE);
        }

        LocalDate afterOneYear = today.plusYears(1);
        if (date.isAfter(afterOneYear)) {
            throw new IllegalArgumentException(BEYOND_ONE_YEAR_ERROR_MESSAGE);
        }
    }

    public static String getOverMaxTitleLengthErrorMessage() {
        return OVER_MAX_TITLE_LENGTH_ERROR_MESSAGE;
    }

    public static String getOverMaxContentLengthErrorMessage() {
        return OVER_MAX_CONTENT_LENGTH_ERROR_MESSAGE;
    }

    public static String getPastDateErrorMessage() {
        return PAST_DATE_ERROR_MESSAGE;
    }

    public static String getBeyondOneYearErrorMessage() {
        return BEYOND_ONE_YEAR_ERROR_MESSAGE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskJpaEntity task = (TaskJpaEntity) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
