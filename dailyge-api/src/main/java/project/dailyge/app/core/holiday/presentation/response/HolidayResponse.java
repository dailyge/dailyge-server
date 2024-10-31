package project.dailyge.app.core.holiday.presentation.response;

import project.dailyge.entity.holiday.HolidayJpaEntity;

public class HolidayResponse {

    private Long holidayId;
    private String name;
    private String date;

    private HolidayResponse() {
    }

    public HolidayResponse(final HolidayJpaEntity holiday) {
        this.holidayId = holiday.getId();
        this.name = holiday.getName();
        this.date = holiday.getDateAsString();
    }

    public Long getHolidayId() {
        return holidayId;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("{\"holidayId\":%d, \"name\":\"%s\", \"date\":\"%s\"}", holidayId, name, date);
    }
}
