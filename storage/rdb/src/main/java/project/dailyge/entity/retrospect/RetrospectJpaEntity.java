package project.dailyge.entity.retrospect;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.dailyge.entity.BaseEntity;
import static jakarta.persistence.EnumType.STRING;

@Getter
@NoArgsConstructor
@Entity(name = "retrospect")
public class RetrospectJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Enumerated(STRING)
    @Column(name = "retrospect_type")
    private RetrospectType retrospectType;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "user_id")
    private Long userId;

    public RetrospectJpaEntity(
        final String title,
        final String content,
        final RetrospectType retrospectType,
        final LocalDate date,
        final Long userId
    ) {
        this.title = title;
        this.content = content;
        this.retrospectType = retrospectType;
        this.date = date;
        this.userId = userId;
    }
}
