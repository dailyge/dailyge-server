package project.dailyge.app.core.retrospect.presentation.response;

import java.util.List;
import lombok.Getter;
import project.dailyge.entity.retrospect.RetrospectJpaEntity;

@Getter
public class RetrospectPageResponse {

    final List<RetrospectResponse> retrospects;
    final int totalPageCount;

    public RetrospectPageResponse(
        final List<RetrospectJpaEntity> retrospects,
        final int totalPageCount
    ) {
        this.retrospects = retrospects.stream()
            .map(RetrospectResponse::new)
            .toList();
        this.totalPageCount = totalPageCount;
    }

    @Override
    public String toString() {
        return String.format(
            "{\"retrospects\":\"%s\",\"totalPageCount\":\"%s\"}",
            retrospects, totalPageCount
        );
    }
}
