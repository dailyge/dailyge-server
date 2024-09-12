package project.dailyge.app.common.configuration.aws.cloudwatch;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.util.CachingDateFormatter;
import io.netty.channel.nio.NioEventLoopGroup;
import static java.lang.System.currentTimeMillis;
import static java.util.Collections.singletonList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.SdkEventLoopGroup;
import static software.amazon.awssdk.regions.Region.AP_NORTHEAST_2;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsAsyncClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.InputLogEvent;
import software.amazon.awssdk.services.cloudwatchlogs.model.PutLogEventsRequest;
import software.amazon.awssdk.services.cloudwatchlogs.model.PutLogEventsResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Getter
public class CloudWatchLogAppender extends AppenderBase<ILoggingEvent> {

    private static final String LOG_GROUP_PREFIX = "dailyge-ol-";
    private static final String LOG_STREAM_PREFIX = "dailyge-api-";
    private static final CachingDateFormatter dateFormatter = new CachingDateFormatter("yyyy-MM-dd");

    private CloudWatchLogsAsyncClient client;
    private String logGroup;
    private final AtomicReference<String> sequenceToken = new AtomicReference<>(null);

    /**
     * 초기화를 위한 생성자로, 외부에서 호출하지 말 것.
     */
    public CloudWatchLogAppender() {
    }

    public void init(
        final String env,
        final String awsAccessKey,
        final String awsSecretKey
    ) {
        this.logGroup = String.format("%s%s", LOG_GROUP_PREFIX, env);
        final StaticCredentialsProvider provider = StaticCredentialsProvider.create(
            AwsBasicCredentials.create(awsAccessKey, awsSecretKey)
        );
        final SdkEventLoopGroup sdkEventLoopGroup = SdkEventLoopGroup.create(new NioEventLoopGroup(5));
        this.client = CloudWatchLogsAsyncClient.builder()
            .httpClientBuilder(NettyNioAsyncHttpClient.builder()
                .maxConcurrency(50)
                .eventLoopGroup(sdkEventLoopGroup)
            )
            .region(AP_NORTHEAST_2)
            .credentialsProvider(provider)
            .build();
    }

    @Override
    protected void append(final ILoggingEvent event) {
        final String date = dateFormatter.format(currentTimeMillis());
        final String logStreamName = String.format("%s%s", LOG_STREAM_PREFIX, date);

        try {
            final InputLogEvent logEvent = InputLogEvent.builder()
                .message(event.getFormattedMessage())
                .timestamp(currentTimeMillis())
                .build();
            final PutLogEventsRequest request = PutLogEventsRequest.builder()
                .logGroupName(logGroup)
                .logStreamName(logStreamName)
                .logEvents(singletonList(logEvent))
                .sequenceToken(sequenceToken.get())
                .build();

            final CompletableFuture<PutLogEventsResponse> future = client.putLogEvents(request);
            future.thenApply(response -> {
                sequenceToken.set(response.nextSequenceToken());
                return response;
            }).exceptionally(exception -> {
                log.error("Failed to send log to CloudWatch: {}", exception.getMessage());
                return null;
            });
        } catch (Exception ex) {
            log.error("ex: {}", ex.getMessage());
        }
    }
}
