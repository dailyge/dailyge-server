package project.dailyge.document.task;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import project.dailyge.document.DocumentBaseEntity;

import java.util.List;

@Getter
@Document(collection = "monthly_tasks")
public class MonthlyTaskDocument extends DocumentBaseEntity {

    @Id
    @Field(name = "_id")
    private String id;

    @Field(name = "user_id")
    private Long userId;

    @Field(name = "year")
    private int year;

    @Field(name = "month")
    private int month;

    @Field(name = "tasks")
    private List<TaskDocument> tasks;

    /**
     * 직렬화를 위한 기본 생성자로, 외부 패키지에서 호출하지 말 것.
     */
    private MonthlyTaskDocument() {
    }

    private MonthlyTaskDocument(
        final String _id,
        final Long userId,
        final int year,
        final int month
    ) {
        this.id = _id;
        this.userId = userId;
        this.year = year;
        this.month = month;
        initOperatingColumns(userId);
    }

    public static MonthlyTaskDocument createMonthlyDocument(
        final String _id,
        final Long userId,
        final int year,
        final int month
    ) {
        return new MonthlyTaskDocument(_id, userId, year, month);
    }
}
