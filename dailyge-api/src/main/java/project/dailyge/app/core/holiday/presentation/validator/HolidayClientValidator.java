package project.dailyge.app.core.holiday.presentation.validator;

import org.springframework.stereotype.Component;

@Component
public class HolidayClientValidator {

    public void validateYear(final int year) {
        if (year <= 0 || year >= 2100) {
            throw new IllegalArgumentException("올바른 년도를 입력해주세요.");
        }
    }
}
