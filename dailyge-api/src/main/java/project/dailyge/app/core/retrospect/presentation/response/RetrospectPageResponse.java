package project.dailyge.app.core.retrospect.presentation.response;

import java.util.List;

public record RetrospectPageResponse(
    List<RetrospectResponse> retrospects,
    int totalPageCount
) {

}
