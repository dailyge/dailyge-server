package project.dailyge.app.core.anniversary.presentation.response;

public class AnniversaryCreateResponse {

    private Long anniversaryId;

    private AnniversaryCreateResponse() {
    }

    public AnniversaryCreateResponse(final Long anniversaryId) {
        this.anniversaryId = anniversaryId;
    }

    public Long getAnniversaryId() {
        return anniversaryId;
    }

    @Override
    public String toString() {
        return String.format("{\"anniversaryId\":%d}", anniversaryId);
    }
}
