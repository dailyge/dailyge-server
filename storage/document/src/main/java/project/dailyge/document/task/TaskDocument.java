package project.dailyge.document.task;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import project.dailyge.document.DocumentBaseEntity;

import java.time.LocalDate;

@Getter
@Document(collection = "tasks")
public class TaskDocument extends DocumentBaseEntity {

    @Id
    private String id;

    @Field(name = "title")
    private String title;

    @Field(name = "content")
    private String content;

    @Field(name = "year")
    private int year;

    @Field(name = "month")
    private int month;

    @Field(name = "date")
    private LocalDate date;

    @Field(name = "status")
    private String status;

    @Field(name = "user_id")
    private Long userId;
}
