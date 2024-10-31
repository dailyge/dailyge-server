package project.dailyge.app.core.note.application.command;

import project.dailyge.entity.note.NoteJpaEntity;

import java.time.LocalDateTime;

public class NoteCreateCommand {

    private String title;
    private String content;
    private LocalDateTime sendAt;
    private String nickname;
    private Long senderId;

    private NoteCreateCommand() {
    }

    public NoteCreateCommand(
        final String title,
        final String content,
        final LocalDateTime sendAt,
        final String nickname,
        final Long senderId
    ) {
        this.title = title;
        this.content = content;
        this.sendAt = sendAt;
        this.nickname = nickname;
        this.senderId = senderId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getSendAt() {
        return sendAt;
    }

    public String getNickname() {
        return nickname;
    }

    public Long getSenderId() {
        return senderId;
    }

    public NoteJpaEntity toEntity(final Long receiverId) {
        return new NoteJpaEntity(title, content, sendAt, senderId, receiverId);
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
            + "\"sendAt\": \""
            + (sendAt != null ? sendAt.toString() : null)
            + "\","
            + "\"nickname\": \""
            + nickname + "\","
            + "\"senderId\": "
            + senderId
            + "}";
    }
}
