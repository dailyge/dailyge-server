package project.dailyge.entity.retrospect;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import project.dailyge.entity.BaseEntity;

import java.time.LocalDateTime;

@Entity(name = "retrospects")
public class RetrospectJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "user_id")
    private Long userId;

    protected RetrospectJpaEntity() {
    }

    public RetrospectJpaEntity(
        final String title,
        final String content,
        final LocalDateTime date,
        final boolean isPublic,
        final Long userId
    ) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.isPublic = isPublic;
        this.userId = userId;
    }

    public RetrospectJpaEntity(
        final Long id,
        final String title,
        final String content,
        final LocalDateTime date,
        final boolean isPublic,
        final Long userId
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.isPublic = isPublic;
        this.userId = userId;
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

    public LocalDateTime getDate() {
        return date;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public Long getUserId() {
        return userId;
    }

    public void update(
        final String title,
        final String content,
        final LocalDateTime date,
        final boolean isPublic
    ) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.isPublic = isPublic;
    }

    public void delete() {
        updateDeletedStatus();
    }
}
