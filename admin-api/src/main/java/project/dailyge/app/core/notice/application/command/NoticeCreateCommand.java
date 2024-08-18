package project.dailyge.app.core.notice.application.command;

import project.dailyge.entity.notice.NoticeJpaEntity;
import project.dailyge.entity.notice.NoticeType;

public record NoticeCreateCommand (
    String title,
    String content,
    NoticeType noticeType,
    Long userId
) {
    public NoticeJpaEntity toEntity() {
        return new NoticeJpaEntity(title, content, noticeType, userId);
    }

    @Override
    public String toString() {
        return String.format(
            "{\"title\":\"%s\",\"content\":\"%s\",\"noticeType\":\"%s\",\"userId\":\"%s\"}",
            title != null ? title : "",
            content != null ? content : "",
            noticeType != null ? noticeType : "",
            userId != null ? userId : ""
        );
    }
}
