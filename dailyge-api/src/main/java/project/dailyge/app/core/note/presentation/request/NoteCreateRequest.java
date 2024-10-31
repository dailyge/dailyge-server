package project.dailyge.app.core.note.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.note.application.command.NoteCreateCommand;

import java.time.LocalDateTime;

public class NoteCreateRequest {

    @NotNull(message = "제목을 입력해주세요.")
    @NotBlank(message = "제목은 공백일 수 없습니다.")
    @Size(max = 50, message = "제목은 50자 이하로 입력해주세요.")
    private String title;

    @NotNull(message = "내용을 입력해주세요.")
    @NotBlank(message = "내용은 공백일 수 없습니다.")
    @Size(max = 200, message = "내용은 200자 이하로 입력해주세요.")
    private String content;

    @NotNull(message = "닉네임을 입력해주세요.")
    @NotBlank(message = "닉네임은 공백일 수 없습니다.")
    @Size(min = 1, max = 20, message = "닉네임은 1자 이상, 20자 이하로 입력해주세요.")
    private String nickname;

    @NotNull(message = "날짜를 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendAt;

    private NoteCreateRequest() {
    }

    public NoteCreateRequest(
        final String title,
        final String content,
        final String nickname
    ) {
        this.title = title;
        this.content = content;
        this.nickname = nickname;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getNickname() {
        return nickname;
    }

    public LocalDateTime getSendAt() {
        return sendAt;
    }

    public NoteCreateCommand toCommand(final DailygeUser dailygeUser) {
        return new NoteCreateCommand(title, content, sendAt, nickname, dailygeUser.getId());
    }

    @Override
    public String toString() {
        return "{"
            + "\"title\": \""
            + title
            + "\","
            + "\"content\": \""
            + content
            + "\","
            + "\"nickname\": \""
            + nickname
            + "\","
            + "\"sendAt\": \""
            + sendAt
            + "\""
            + "}";
    }
}
