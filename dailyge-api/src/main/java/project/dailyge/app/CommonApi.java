package project.dailyge.app;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.codeandmessage.CodeAndMessageConfig;
import project.dailyge.entity.codeandmessage.CodeAndMessageEntityWriteService;
import project.dailyge.entity.codeandmessage.CodeAndMessages;

@RequiredArgsConstructor
@RequestMapping(path = {"/api"})
@PresentationLayer(value = "CommonApi")
public class CommonApi {

    private final CodeAndMessageEntityWriteService codeAndMessageWriteService;
    private final CodeAndMessageConfig codeAndMessageConfig;

    @GetMapping(path = {"/health-check"})
    public ApiResponse<String> healthCheck() {
        return ApiResponse.from(OK);
    }

    // TODO. 추후 권한 점검 추가.
    @PostMapping(path = {"/status"})
    public ApiResponse<Void> saveAll(@LoginUser final DailygeUser dailygeUser) {
        final CodeAndMessages codeAndMessages = new CodeAndMessages(codeAndMessageConfig.getAllCodeAndMessages());
        codeAndMessageWriteService.saveAll(codeAndMessages);
        return ApiResponse.from(OK);
    }
}
