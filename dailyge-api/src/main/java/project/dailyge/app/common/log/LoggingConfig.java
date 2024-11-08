package project.dailyge.app.common.log;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

@Profile("!test")
@Configuration
public class LoggingConfig {

    private final MongoTemplate mongoTemplate;

    public LoggingConfig(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Bean
    public DailygeLogAppender dailygeLogAppender() {
        final DailygeLogAppender dailygeLogAppender = new DailygeLogAppender();
        dailygeLogAppender.init(mongoTemplate);
        dailygeLogAppender.start();
        return dailygeLogAppender;
    }

    @Bean(name = "asyncAppender")
    public AsyncAppender asyncAppender(final DailygeLogAppender dailygeLogAppender) {
        final AsyncAppender asyncAppender = new AsyncAppender();
        asyncAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        asyncAppender.addAppender(dailygeLogAppender);
        asyncAppender.setQueueSize(8_000);
        asyncAppender.setDiscardingThreshold(0);
        asyncAppender.start();
        return asyncAppender;
    }

    @Bean
    public ApplicationListener<ApplicationStartedEvent> logbackConfigListener(final AsyncAppender asyncAppender) {
        return event -> {
            final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            final Logger rootLogger = loggerContext.getLogger("ROOT");
            rootLogger.addAppender(asyncAppender);
        };
    }
}
