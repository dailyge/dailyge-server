package project.dailyge.app.core.anniversary.presentation.response;

import lombok.Getter;

@Getter
public class AnniversaryCreateResponse {

    private Long anniversaryId;

    private AnniversaryCreateResponse() {
    }

    public AnniversaryCreateResponse(final Long anniversaryId) {
        this.anniversaryId = anniversaryId;
    }

    @Override
    public String toString() {
        return String.format("{\"anniversaryId\":%d}", anniversaryId);
    }
}
