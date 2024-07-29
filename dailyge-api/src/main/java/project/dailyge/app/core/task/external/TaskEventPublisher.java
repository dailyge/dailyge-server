package project.dailyge.app.core.task.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.dailyge.entity.common.EventPublisher;
import project.dailyge.entity.task.TaskEvent;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskEventPublisher implements EventPublisher<TaskEvent> {

    private final SnsClient snsClient;
    private final ObjectMapper objectMapper;

    @Override
    public void publishExternalEvent(final TaskEvent event) {
        try {
            log.info(event.toString());
            PublishRequest request = PublishRequest.builder()
                .topicArn("arn:aws:sns:ap-northeast-2:563951805858:alarm")
                .message(objectMapper.writeValueAsString(event))
                .build();

            PublishResponse response = snsClient.publish(request);
        } catch (Exception ex) {
            System.out.println("EXCEPTION");
            System.out.println(ex.getMessage());
        }
    }
}
