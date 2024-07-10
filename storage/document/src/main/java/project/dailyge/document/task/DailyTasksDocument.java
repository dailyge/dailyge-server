package project.dailyge.document.task;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import project.dailyge.document.DocumentBaseEntity;

import java.util.List;

@Getter
@Document(collection = "daily_tasks")
public class DailyTasksDocument extends DocumentBaseEntity {

    @Id
    private String id;

    @Field(name = "user_id")
    private Long userId;

    @Field(name = "year")
    private int year;

    @Field(name = "month")
    private int month;

    private List<TaskDocument> tasks;
}
