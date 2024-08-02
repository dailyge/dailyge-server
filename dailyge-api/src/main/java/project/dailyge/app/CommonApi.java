package project.dailyge.app;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.codeandmessage.CodeAndMessageConfig;
import project.dailyge.entity.codeandmessage.CodeAndMessageEntityWriteService;
import project.dailyge.entity.codeandmessage.CodeAndMessages;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/api"})
public class CommonApi {

    private final CodeAndMessageEntityWriteService codeAndMessageWriteService;
    private final CodeAndMessageConfig codeAndMessageConfig;

    @GetMapping(path = {"/health-check"})
    public ApiResponse<String> healthCheck() {
        return ApiResponse.from(OK);
    }

    @GetMapping(path = {"/health-check/v2"})
    public ApiResponse<String> healthCheckV2() {
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
