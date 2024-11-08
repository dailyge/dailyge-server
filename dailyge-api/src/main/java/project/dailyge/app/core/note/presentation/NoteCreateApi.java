package project.dailyge.app.core.note.presentation;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.CREATED;
import project.dailyge.app.common.annotation.LoginUser;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.note.facade.NoteFacade;
import project.dailyge.app.core.note.presentation.request.NoteCreateRequest;
import project.dailyge.app.core.note.presentation.response.NoteCreateResponse;

@RequestMapping(path = "/api/notes")
@PresentationLayer(value = "NoteCreateApi")
public class NoteCreateApi {

    private final NoteFacade noteFacade;

    public NoteCreateApi(final NoteFacade noteFacade) {
        this.noteFacade = noteFacade;
    }

    @PostMapping
    public ApiResponse<NoteCreateResponse> createNote(
        @LoginUser final DailygeUser dailygeUser,
        @Valid @RequestBody final NoteCreateRequest request
    ) {
        final Long newNoteId = noteFacade.save(dailygeUser, request.toCommand(dailygeUser), 30);
        final NoteCreateResponse payload = new NoteCreateResponse(newNoteId);
        return ApiResponse.from(CREATED, payload);
    }
}
