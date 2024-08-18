package project.dailyge.app.core.notice.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import project.dailyge.app.core.notice.application.command.NoticeCreateCommand;
import project.dailyge.entity.notice.NoticeType;

public record NoticeCreateRequest(
    @NotNull
    @NotBlank
    @Size(max = 30)
    String title,

    @NotNull
    @NotBlank
    @Size(max = 3000)
    String content,

    @NotNull
    NoticeType noticeType
) {
    public NoticeCreateCommand toCommand(final Long userId) {
        return new NoticeCreateCommand(title, content, noticeType, userId);
    }

    @Override
    public String toString() {
        return String.format(
            "{\"title\":\"%s\",\"content\":\"%s\",\"noticeType\":\"%s\"}",
            title != null ? title : "",
            content != null ? content : "",
            noticeType != null ? noticeType : ""
        );
    }
}
