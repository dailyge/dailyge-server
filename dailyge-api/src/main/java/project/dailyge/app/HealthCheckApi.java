package project.dailyge.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static project.dailyge.app.common.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.response.ApiResponse;

@RestController
@RequestMapping("/api/health-check")
public class HealthCheckApi {

    @GetMapping
    public ApiResponse<String> healthCheck() {
        return ApiResponse.from(OK);
    }
}
