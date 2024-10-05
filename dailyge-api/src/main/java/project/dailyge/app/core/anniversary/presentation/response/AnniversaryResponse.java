package project.dailyge.app.core.anniversary.presentation.response;

import lombok.Getter;
import project.dailyge.entity.anniversary.AnniversaryJpaEntity;

@Getter
public class AnniversaryResponse {

    private Long anniversaryId;
    private String name;
    private String date;

    private AnniversaryResponse() {
    }

    public AnniversaryResponse(final AnniversaryJpaEntity anniversary) {
        this.anniversaryId = anniversary.getId();
        this.name = anniversary.getName();
        this.date = anniversary.getDateAsString();
    }

    @Override
    public String toString() {
        return String.format("{\"anniversaryId\":%d, \"name\":\"%s\", \"date\":\"%s\"}", anniversaryId, name, date);
    }
}
