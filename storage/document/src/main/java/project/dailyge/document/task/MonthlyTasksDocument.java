package project.dailyge.document.task;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import project.dailyge.document.DocumentBaseEntity;

import java.time.LocalDate;
import java.util.List;

@Getter
@Document(collection = "daily_tasks")
public class MonthlyTasksDocument extends DocumentBaseEntity {

    @Id
    private String id;

    @Field(name = "user_id")
    private Long userId;

    @Field(name = "date")
    private LocalDate date;

    private List<DailyTasksDocument> dailyTasks;
}
