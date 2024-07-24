package project.dailyge.app.response;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class CodeAndMessagesResponse {

    private final Set<String> domains;
    private final Map<String, List<CodeAndMessageResponse>> map;

    public CodeAndMessagesResponse(
        final Map<String, List<CodeAndMessageResponse>> codeAndMessages
    ) {
        this.domains = codeAndMessages.keySet();
        this.map = codeAndMessages;
    }
}
