package project.dailyge.app.core.note.presentation.response;

import project.dailyge.entity.note.NoteJpaEntity;

public class SentNoteResponse {

    private Long noteId;
    private String title;
    private String content;
    private boolean read;

    private SentNoteResponse() {
    }

    public SentNoteResponse(final NoteJpaEntity note) {
        this.noteId = note.getId();
        this.title = note.getTitle();
        this.content = note.getContent();
        this.read = note.isRead();
    }

    public Long getNoteId() {
        return noteId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean isRead() {
        return read;
    }

    @Override
    public String toString() {
        return String.format(
            "{\"noteId\": \"%d\", \"title\": \"%s\", \"content\": \"%s\", \"isRead\": %b}",
            noteId,
            title,
            content,
            read
        );
    }
}
