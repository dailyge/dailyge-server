package project.dailyge.entity.task;

import java.util.List;

public class DatePattern {
    private static final int MONDAY = 1;
    private static final int SUNDAY = 7;
    private static final int FIRST_DAY = 1;
    private static final int LAST_DAY = 31;

    private final List<Integer> days;

    private DatePattern(final List<Integer> days) {
        this.days = days;
    }

    public static DatePattern create(
        final List<Integer> days,
        final RecurrenceType type
    ) {
        if (type.equals(RecurrenceType.DAILY) && days != null) {
            throw new IllegalArgumentException("days가 적용되지 않습니다.");
        }
        if (type.equals(RecurrenceType.WEEKLY)) {
            validateWeek(days);
            return new DatePattern(days);
        }
        if (type.equals(RecurrenceType.MONTHLY)) {
            validateMonth(days);
            return new DatePattern(days);
        }
        throw new IllegalArgumentException();
    }

    private static void validateWeek(final List<Integer> days) {
        if (days == null) {
            throw new IllegalArgumentException();
        }
        for (final int day : days) {
            if (!(MONDAY <= day && day <= SUNDAY)) {
                throw new IllegalArgumentException();
            }
        }
    }

    private static void validateMonth(final List<Integer> days) {
        if (days == null) {
            throw new IllegalArgumentException();
        }
        for (final int day : days) {
            if (!(FIRST_DAY <= day && day <= LAST_DAY)) {
                throw new IllegalArgumentException();
            }
        }
    }
}
