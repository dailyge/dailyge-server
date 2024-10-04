package project.dailyge.entity.retrospect;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.dailyge.entity.BaseEntity;

@Getter
@NoArgsConstructor
@Entity(name = "retrospects")
public class RetrospectJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "user_id")
    private Long userId;

    public RetrospectJpaEntity(
        final String title,
        final String content,
        final LocalDate startDate,
        final LocalDate endDate,
        final boolean isPublic,
        final Long userId
    ) {
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isPublic = isPublic;
        this.userId = userId;
    }

    public RetrospectJpaEntity(
        final Long id,
        final String title,
        final String content,
        final LocalDate startDate,
        final LocalDate endDate,
        final boolean isPublic,
        final Long userId
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isPublic = isPublic;
        this.userId = userId;
    }
}
