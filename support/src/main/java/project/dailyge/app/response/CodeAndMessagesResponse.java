package project.dailyge.app.response;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CodeAndMessagesResponse {

    private final Set<String> domains;
    private final Map<String, List<CodeAndMessageResponse>> map;

    public CodeAndMessagesResponse(final Map<String, List<CodeAndMessageResponse>> codeAndMessages) {
        this.domains = codeAndMessages.keySet();
        this.map = codeAndMessages;
    }

    public Set<String> getDomains() {
        return domains;
    }

    public Map<String, List<CodeAndMessageResponse>> getMap() {
        return map;
    }
}
