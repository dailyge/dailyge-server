package project.dailyge.app.core.task.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import project.dailyge.app.common.annotation.External;
import project.dailyge.entity.common.EventPublisher;
import project.dailyge.entity.task.TaskEvent;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Slf4j
@External
@RequiredArgsConstructor
public class TaskEventPublisher implements EventPublisher<TaskEvent> {

    @Value("${application.amazon.sns.topic-arn}")
    private String topicArn;

    private final SnsClient snsClient;
    private final ObjectMapper objectMapper;

    @Override
    public void publishExternalEvent(final TaskEvent event) {
        try {
            final PublishRequest request = PublishRequest.builder()
                .topicArn(topicArn)
                .message(objectMapper.writeValueAsString(event))
                .build();

            final PublishResponse response = snsClient.publish(request);
        } catch (Exception ex) {
        }
    }
}
