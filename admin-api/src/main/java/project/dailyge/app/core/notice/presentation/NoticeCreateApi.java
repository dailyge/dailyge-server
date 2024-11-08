package project.dailyge.app.core.notice.presentation;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.codeandmessage.CommonCodeAndMessage;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.UN_AUTHORIZED;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.exception.CommonException;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.notice.application.NoticeWriteService;
import project.dailyge.app.core.notice.application.usecase.NoticeWriteUseCase;
import project.dailyge.app.core.notice.presentation.request.NoticeCreateRequest;
import project.dailyge.app.core.notice.presentation.response.NoticeCreateResponse;

@PresentationLayer
@RequestMapping(path = "/api")
public class NoticeCreateApi {

    private final NoticeWriteService noticeWriteService;

    public NoticeCreateApi(final NoticeWriteUseCase noticeWriteService) {
        this.noticeWriteService = noticeWriteService;
    }

    @PostMapping("/notice")
    public ApiResponse<NoticeCreateResponse> saveNotice(
        @LoginUser final DailygeUser dailygeUser,
        @Valid @RequestBody final NoticeCreateRequest request
    ) {
        if (!dailygeUser.isAdmin()) {
            throw CommonException.from(UN_AUTHORIZED);
        }
        final Long noticeId = noticeWriteService.save(request.toCommand(dailygeUser.getUserId()));
        final NoticeCreateResponse payload = new NoticeCreateResponse(noticeId);
        return ApiResponse.from(CommonCodeAndMessage.CREATED, payload);
    }
}
