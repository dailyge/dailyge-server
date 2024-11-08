package project.dailyge.app.core.anniversary.presentation.response;

import project.dailyge.entity.anniversary.AnniversaryJpaEntity;

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

    public Long getAnniversaryId() {
        return anniversaryId;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("{\"anniversaryId\":%d, \"name\":\"%s\", \"date\":\"%s\"}", anniversaryId, name, date);
    }
}
