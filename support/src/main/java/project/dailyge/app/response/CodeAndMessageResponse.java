package project.dailyge.app.response;

public record CodeAndMessageResponse(
    Long id,
    String domain,
    String name,
    String message,
    int code
) {
}
