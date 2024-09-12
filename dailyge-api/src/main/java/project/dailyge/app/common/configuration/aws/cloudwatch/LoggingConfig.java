package project.dailyge.app.common.configuration.aws.cloudwatch;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {

    private final String awsAccessKey;
    private final String awsSecretKey;

    public LoggingConfig(
        @Value("${spring.cloud.aws.credentials.access-key}") final String awsAccessKey,
        @Value("${spring.cloud.aws.credentials.secret-key}") final String awsSecretKey
    ) {
        this.awsAccessKey = awsAccessKey;
        this.awsSecretKey = awsSecretKey;
    }

    @Bean
    public CloudWatchLogAppender dateBasedCloudWatchAppender() {
        final CloudWatchLogAppender appender = new CloudWatchLogAppender();
        appender.init(awsAccessKey, awsSecretKey);
        final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        appender.setContext(loggerContext);
        return appender;
    }

    @Bean
    public AsyncAppender asyncAppender(final CloudWatchLogAppender cloudWatchAppender) {
        cloudWatchAppender.start();
        final AsyncAppender asyncAppender = new AsyncAppender();
        asyncAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        asyncAppender.setQueueSize(8_000);
        asyncAppender.setDiscardingThreshold(0);
        asyncAppender.addAppender(cloudWatchAppender);
        asyncAppender.start();
        return asyncAppender;
    }

    @Bean
    public ApplicationListener<ApplicationStartedEvent> logbackConfigListener(final AsyncAppender asyncAppender) {
        return event -> {
            final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            ch.qos.logback.classic.Logger rootLogger = loggerContext.getLogger("ROOT");
            rootLogger.addAppender(asyncAppender);
        };
    }
}
