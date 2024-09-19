package project.dailyge.document.log;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Document(collection = "system_logs")
public class SystemLogDocument {

    @Id
    @Field(name = "_id")
    private String id;
    private LocalDateTime time;
    private String level;
    private String systemLog;

    protected SystemLogDocument() {
    }

    public SystemLogDocument(
        final String id,
        final LocalDateTime time,
        final String level,
        final String systemLog
    ) {
        this.id = id;
        this.time = time;
        this.level = level;
        this.systemLog = systemLog;
    }
}
