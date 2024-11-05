package project.dailyge.app.core.note.presentation;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.NO_CONTENT;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.note.application.NoteWriteService;

@RequestMapping(path = "/api/notes")
@PresentationLayer(value = "NoteDeleteApi")
public class NoteDeleteApi {

    private final NoteWriteService noteWriteService;

    public NoteDeleteApi(final NoteWriteService noteWriteService) {
        this.noteWriteService = noteWriteService;
    }

    @DeleteMapping(path = "/{noteId}/received")
    public ApiResponse<Void> deleteReceivedNote(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(value = "noteId") final Long noteId
    ) {
        noteWriteService.deleteReceivedNoteById(dailygeUser, noteId);
        return ApiResponse.from(NO_CONTENT);
    }

    @DeleteMapping(path = "/{noteId}/sent")
    public ApiResponse<Void> deleteSentNote(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(value = "noteId") final Long noteId
    ) {
        noteWriteService.deleteSentNoteById(dailygeUser, noteId);
        return ApiResponse.from(NO_CONTENT);
    }
}
