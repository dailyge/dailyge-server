package project.dailyge.app.core.note.presentation.response;

import project.dailyge.entity.note.NoteJpaEntity;

public class ReceivedNoteResponse {

    private Long noteId;
    private String title;
    private String content;

    private ReceivedNoteResponse() {
    }

    public ReceivedNoteResponse(final NoteJpaEntity note) {
        this.noteId = note.getId();
        this.title = note.getTitle();
        this.content = note.getContent();
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

    @Override
    public String toString() {
        return String.format(
            "{\"noteId\": \"%d\", \"title\": \"%s\", \"content\": \"%s\"}",
            noteId,
            title,
            content
        );
    }
}
