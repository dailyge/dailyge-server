package project.dailyge.app.core.retrospect.presentation.response;

import project.dailyge.entity.retrospect.RetrospectJpaEntity;

import java.util.List;

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

    public List<RetrospectResponse> getRetrospects() {
        return retrospects;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    @Override
    public String toString() {
        return String.format(
            "{\"retrospects\":\"%s\",\"totalPageCount\":\"%s\"}",
            retrospects, totalPageCount
        );
    }
}
