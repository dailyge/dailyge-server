package project.dailyge.entity.notice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import static jakarta.persistence.EnumType.STRING;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import project.dailyge.entity.BaseEntity;

@Entity(name = "notice")
public class NoticeJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Enumerated(STRING)
    @Column(name = "notice_type")
    private NoticeType noticeType;

    @Column(name = "user_id")
    private Long userId;

    protected NoticeJpaEntity() {
    }

    public NoticeJpaEntity(
        final Long id,
        final String title,
        final String content,
        final NoticeType noticeType,
        final Long userId
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.noticeType = noticeType;
        this.userId = userId;
    }

    public NoticeJpaEntity(
        final String title,
        final String content,
        final NoticeType noticeType,
        final Long userId
    ) {
        this.title = title;
        this.content = content;
        this.noticeType = noticeType;
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

    public NoticeType getNoticeType() {
        return noticeType;
    }

    public Long getUserId() {
        return userId;
    }
}
