package project.dailyge.app.core.holiday.presentation.response;

import lombok.Getter;
import project.dailyge.entity.holiday.HolidayJpaEntity;

@Getter
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

    @Override
    public String toString() {
        return String.format("{\"holidayId\":%d, \"name\":\"%s\", \"date\":\"%s\"}", holidayId, name, date);
    }
}
