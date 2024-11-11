package project.dailyge.app.core.note.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.api.CursorPagingResponse;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.annotation.CursorPageable;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.note.application.NoteReadService;
import project.dailyge.app.core.note.presentation.response.ReceivedNoteResponse;
import project.dailyge.app.core.note.presentation.response.SentNoteResponse;
import project.dailyge.app.paging.Cursor;
import project.dailyge.entity.note.NoteJpaEntity;

@RequestMapping(path = "/api/notes")
@PresentationLayer(value = "NoteReadApi")
public class NoteReadApi {

    private final NoteReadService noteReadService;

    public NoteReadApi(final NoteReadService noteReadService) {
        this.noteReadService = noteReadService;
    }

    @GetMapping(path = "/{noteId}/sent")
    public ApiResponse<SentNoteResponse> findSentNoteById(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "noteId") final Long noteId
    ) {
        final NoteJpaEntity findNote = noteReadService.findSentNotesById(dailygeUser, noteId);
        final SentNoteResponse payload = new SentNoteResponse(findNote);
        return ApiResponse.from(OK, payload);
    }

    @GetMapping(path = "/sent")
    public ApiResponse<CursorPagingResponse<NoteJpaEntity>> findSentNotesById(
        @LoginUser final DailygeUser dailygeUser,
        @CursorPageable final Cursor cursor
    ) {
        final CursorPagingResponse<NoteJpaEntity> payload = noteReadService.findSentNotesById(dailygeUser, cursor);
        return ApiResponse.from(OK, payload);
    }

    @GetMapping(path = "/{noteId}/received")
    public ApiResponse<ReceivedNoteResponse> findReceivedNoteById(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "noteId") final Long noteId
    ) {
        final NoteJpaEntity findNote = noteReadService.findReceivedNoteById(dailygeUser, noteId);
        final ReceivedNoteResponse payload = new ReceivedNoteResponse(findNote);
        return ApiResponse.from(OK, payload);
    }

    @GetMapping(path = "/received")
    public ApiResponse<CursorPagingResponse<NoteJpaEntity>> findReceivedNotesById(
        @LoginUser final DailygeUser dailygeUser,
        @CursorPageable final Cursor cursor
    ) {
        final CursorPagingResponse<NoteJpaEntity> payload = noteReadService.findReceivedNotesById(dailygeUser, cursor);
        return ApiResponse.from(OK, payload);
    }
}
