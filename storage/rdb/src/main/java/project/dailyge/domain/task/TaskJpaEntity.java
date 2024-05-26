package project.dailyge.domain.task;

import jakarta.persistence.*;
import lombok.*;
import project.dailyge.domain.*;

import java.time.*;
import java.util.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tasks")
public class TaskJpaEntity extends BaseEntity {

    private static final int MAX_TITLE_LENGTH = 150;
    private static final int MAX_CONTENT_LENGTH = 2_500;
    private static final int ONE_YEAR = 12;

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
        String title,
        String content,
        LocalDate date,
        TaskStatus status,
        Long userId
    ) {
        validate(title, content, date);
        this.title = title;
        this.content = content;
        this.status = status;
        this.date = date;
        this.year = date.getYear();
        this.month = date.getMonthValue();
        this.userId = userId;
    }

    private void validate(
        String title,
        String content,
        LocalDate date
    ) {
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("입력 가능한 최대 제목 길이를 초과했습니다.");
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException("입력 가능한 최대 내용 길이를 초과했습니다.");
        }

        LocalDate today = LocalDate.now();
        if (date.isBefore(today)) {
            throw new IllegalArgumentException("과거 날짜는 등록할 수 없습니다.");
        }

        LocalDate afterOneYear = today.plusYears(1);
        if (date.isAfter(afterOneYear)) {
            throw new IllegalArgumentException("1년 이내의 일정을 등록해주세요.");
        }
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
