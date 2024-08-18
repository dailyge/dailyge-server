package project.dailyge.app.core.notice.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.codeandmessage.CommonCodeAndMessage;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.notice.application.service.NoticeWriteService;
import project.dailyge.app.core.notice.presentation.request.NoticeCreateRequest;
import project.dailyge.app.core.notice.presentation.response.NoticeCreateResponse;

@PresentationLayer
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class NoticeCreateApi {

    private final NoticeWriteService noticeWriteService;

    @PostMapping("/notice")
    public ApiResponse<NoticeCreateResponse>  saveNotice(
        @LoginUser final DailygeUser dailygeUser,
        @RequestBody final NoticeCreateRequest request
    ) {
        if (dailygeUser.isAdmin()) {
            throw new UnAuthorizedException();
        }
        final Long noticeId = noticeWriteService.save(request.toCommand(dailygeUser.getUserId()));
        final NoticeCreateResponse payload = new NoticeCreateResponse(noticeId);
        return ApiResponse.from(CommonCodeAndMessage.CREATED, payload);
    }
}
