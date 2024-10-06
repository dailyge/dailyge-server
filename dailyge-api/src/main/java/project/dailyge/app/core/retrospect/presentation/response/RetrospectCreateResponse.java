package project.dailyge.app.core.retrospect.presentation.response;

public record RetrospectCreateResponse(Long retrospectId) {

    @Override
    public String toString() {
        return String.format("{\"retrospectId\":\"%s\"}", retrospectId);
    }
}
