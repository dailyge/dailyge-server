package project.dailyge.app.core.codeandmessage.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.response.CodeAndMessagesResponse;
import project.dailyge.entity.codeandmessage.CodeAndMessageJpaEntity;
import project.dailyge.entity.codeandmessage.CodeAndMessageReadService;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import static project.dailyge.app.core.codeandmessage.presentation.CodeAndMessageUtils.convertToMap;

@PresentationLayer
@RequiredArgsConstructor
@RequestMapping(path = "/api/codeandmessages")
public class CodeAndMessageReadApi {

    private final CodeAndMessageReadService codeAndMessageReadService;

    @GetMapping
    public ApiResponse<CodeAndMessagesResponse> findCodeAndMessages() {
        final List<CodeAndMessageJpaEntity> findCodeAndMessages = codeAndMessageReadService.findAll();
        final CodeAndMessagesResponse payload = new CodeAndMessagesResponse(convertToMap(findCodeAndMessages));
        return ApiResponse.from(OK, payload);
    }
}
