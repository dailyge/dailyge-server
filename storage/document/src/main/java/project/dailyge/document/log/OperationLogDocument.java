package project.dailyge.document.log;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Document(collection = "operation_logs")
public class OperationLogDocument {

    @Id
    @Field(name = "_id")
    private String id;
    private String order;
    private String layer;
    private String path;
    private String method;
    private String traceId;
    private String ip;
    private String visitor;
    private final LocalDateTime time;
    private String duration;
    private Object context;
    private String level;

    public OperationLogDocument(
        final String order,
        final String layer,
        final String path,
        final String method,
        final String traceId,
        final String ip,
        final String visitor,
        final LocalDateTime time,
        final String duration,
        final Object context,
        final String level
    ) {
        this.id = traceId;
        this.order = order;
        this.layer = layer;
        this.path = path;
        this.method = method;
        this.traceId = traceId;
        this.ip = ip;
        this.visitor = visitor;
        this.time = time;
        this.duration = duration;
        this.context = context;
        this.level = level;
    }
}
