package project.dailyge.document.task;

import static java.util.Locale.KOREA;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;
import project.dailyge.document.DocumentBaseEntity;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;

import java.time.LocalDate;
import java.time.temporal.WeekFields;

@Getter
public class TaskDocument extends DocumentBaseEntity {

    private static final WeekFields weekFields = WeekFields.of(KOREA);

    @Field(name = "_id")
    private String _id;

    @Field(name = "monthly_task_id")
    private String monthlyTaskId;

    @Field(name = "title")
    private String title;

    @Field(name = "content")
    private String content;

    @Field(name = "year")
    private int year;

    @Field(name = "month")
    private int month;

    @Field(name = "day")
    private int day;

    @Field(name = "week_of_month")
    private int weekOfMonth;

    @Field(name = "status")
    private String status;

    @Field(name = "user_id")
    private Long userId;

    /**
     * 직렬화를 위한 기본 생성자로, 외부 패키지에서 호출하지 말 것.
     */
    protected TaskDocument() {
    }

    public TaskDocument(
        final String monthlyTaskId,
        final String title,
        final String content,
        final LocalDate date,
        final String status,
        final Long userId
    ) {
        this._id = createTimeBasedUUID();
        this.monthlyTaskId = monthlyTaskId;
        this.title = title;
        this.content = content;
        this.year = date.getYear();
        this.month = date.getMonthValue();
        this.day = date.getDayOfMonth();
        this.weekOfMonth = getWeekOfMonth(date);
        this.status = status;
        this.userId = userId;
        initOperatingColumns(userId);
    }

    public String getId() {
        return _id;
    }

    public LocalDate getLocalDate() {
        return LocalDate.of(year, month, day);
    }

    private int getWeekOfMonth(final LocalDate date) {
        return date.get(weekFields.weekOfMonth());
    }
}
