package project.dailyge.entity.monthly_goal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.dailyge.entity.BaseEntity;

import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity(name = "monthly_gols")
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

    @Column(name = "user_id")
    private Long userId;

    public MonthlyGoalJpaEntity(
        final Long id,
        final String title,
        final String content,
        final boolean done,
        final Long userId
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.done = done;
        this.userId = userId;
    }

    public MonthlyGoalJpaEntity(
        final String title,
        final String content,
        final Long userId
    ) {
        this(null, title, content, false, userId);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MonthlyGoalJpaEntity that = (MonthlyGoalJpaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
