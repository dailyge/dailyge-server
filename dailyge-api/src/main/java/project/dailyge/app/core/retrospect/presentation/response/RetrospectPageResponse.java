package project.dailyge.app.core.retrospect.presentation.response;

import java.util.List;

public record RetrospectPageResponse(
    List<RetrospectResponse> retrospects,
    int totalPageCount
) {

    @Override
    public String toString() {
        return String.format(
            "{\"retrospects\":\"%s\",\"totalPageCount\":\"%s\"}",
            retrospects, totalPageCount
        );
    }
}
