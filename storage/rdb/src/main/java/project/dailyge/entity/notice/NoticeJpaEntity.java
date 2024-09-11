package project.dailyge.entity.notice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.dailyge.entity.BaseEntity;
import static jakarta.persistence.EnumType.STRING;

@Getter
@NoArgsConstructor
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
}
