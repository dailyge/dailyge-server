package project.dailyge.app.common.utils;

import java.time.LocalDate;
import java.time.Year;

public final class DateUtils {

    private static final int YEAR_START_DAY = 1;

    private DateUtils() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static LocalDate getYearStartDate(final int year) {
        return Year.of(year).atDay(YEAR_START_DAY);
    }

    public static LocalDate getYearEndDate(final int year) {
        return Year.of(year).atDay(Year.of(year).length());
    }
}
