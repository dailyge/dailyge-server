package project.dailyge.app.common.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import static io.netty.util.internal.StringUtil.EMPTY_STRING;
import static java.time.LocalDateTime.now;
import static java.util.concurrent.TimeUnit.DAYS;
import lombok.extern.slf4j.Slf4j;
import static org.springframework.data.domain.Sort.Direction.ASC;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.scheduling.annotation.Scheduled;
import static project.dailyge.app.constant.LogConstant.ARGS;
import static project.dailyge.app.constant.LogConstant.CONTEXT;
import static project.dailyge.app.constant.LogConstant.DURATION;
import static project.dailyge.app.constant.LogConstant.IP;
import static project.dailyge.app.constant.LogConstant.LAYER;
import static project.dailyge.app.constant.LogConstant.METHOD;
import static project.dailyge.app.constant.LogConstant.ORDER;
import static project.dailyge.app.constant.LogConstant.PATH;
import static project.dailyge.app.constant.LogConstant.RESULT;
import static project.dailyge.app.constant.LogConstant.TIME;
import static project.dailyge.app.constant.LogConstant.TRACE_ID;
import static project.dailyge.app.constant.LogConstant.VISITOR;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;
import project.dailyge.document.log.OperationLogDocument;
import project.dailyge.document.log.SystemLogDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class DailygeLogAppender extends AppenderBase<ILoggingEvent> {

    private final BlockingQueue<OperationLogDocument> queue = new LinkedBlockingQueue<>();
    private MongoTemplate mongoTemplate;
    private final Gson gson = new Gson();

    @Override
    protected void append(final ILoggingEvent event) {
        try {
            final String message = event.getFormattedMessage();
            if (!isOperationLog(message)) {
                final SystemLogDocument log = createSystemLog(event, message);
                mongoTemplate.save(log);
                return;
            }
            final JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            final String order = getJsonValue(jsonObject, ORDER);
            final String layer = getJsonValue(jsonObject, LAYER);
            final String path = getJsonValue(jsonObject, PATH);
            final String method = getJsonValue(jsonObject, METHOD);
            final String duration = getJsonValue(jsonObject, DURATION);
            final String traceId = getJsonValue(jsonObject, TRACE_ID);
            final String ip = getJsonValue(jsonObject, IP);
            final String level = event.getLevel().toString();
            final String userId = getJsonValue(jsonObject, VISITOR);
            final String args = jsonObject.has(CONTEXT) ? gson.toJson(jsonObject.getAsJsonObject(CONTEXT).get(ARGS)) : null;
            final String result = jsonObject.has(CONTEXT) ? gson.toJson(jsonObject.getAsJsonObject(CONTEXT).get(RESULT)) : null;

            final OperationLogDocument operationLog = createOperationLog(
                order, layer, path, method, traceId, ip, userId, duration, args, result, level
            );
            queue.add(operationLog);
        } catch (Exception ex) {
            log.error("Log parsing failed: {}", ex.getMessage());
        }
    }

    private static SystemLogDocument createSystemLog(
        final ILoggingEvent event,
        final String message
    ) {
        return new SystemLogDocument(createTimeBasedUUID(), now(), event.getLevel().toString(), message);
    }

    private OperationLogDocument createOperationLog(
        final String order,
        final String layer,
        final String path,
        final String method,
        final String traceId,
        final String ip,
        final String userId,
        final String duration,
        final String args,
        final String result,
        final String level
    ) {
        return new OperationLogDocument(
            order, layer, path, method, traceId, ip, userId, now(), duration, createContextJson(args, result), level
        );
    }

    private String createContextJson(
        final String args,
        final String result
    ) {
        final JsonElement argsJson = JsonParser.parseString(args);
        final JsonElement resultJson = JsonParser.parseString(result);
        final JsonObject contextJson = new JsonObject();
        contextJson.add(ARGS, argsJson);
        contextJson.add(RESULT, resultJson);
        return gson.toJson(contextJson);
    }

    private String getJsonValue(
        final JsonObject jsonObject,
        final String fieldName
    ) {
        final JsonElement field = jsonObject.get(fieldName);
        return field != null ? field.getAsString() : EMPTY_STRING;
    }

    private boolean isOperationLog(final String message) {
        return message.trim().startsWith("{") && message.trim().endsWith("}");
    }

    public void init(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        mongoTemplate.indexOps(OperationLogDocument.class)
            .ensureIndex(new Index().on(TIME, ASC).expire(3, DAYS));
    }

    @Scheduled(fixedDelay = 5_000)
    private synchronized void scheduler() {
        if (queue.isEmpty()) {
            return;
        }
        final List<OperationLogDocument> logs = new ArrayList<>();
        queue.drainTo(logs);
        mongoTemplate.insertAll(logs);
    }
}
