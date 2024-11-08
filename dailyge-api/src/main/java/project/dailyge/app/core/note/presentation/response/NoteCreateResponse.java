package project.dailyge.app.core.note.presentation.response;


public class NoteCreateResponse {

    private Long noteId;

    private NoteCreateResponse() {
    }

    public NoteCreateResponse(final Long noteId) {
        this.noteId = noteId;
    }

    public Long getNoteId() {
        return noteId;
    }
}
