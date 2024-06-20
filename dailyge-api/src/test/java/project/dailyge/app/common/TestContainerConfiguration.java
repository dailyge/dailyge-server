package project.dailyge.app.common;

import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@Slf4j
public class TestContainerConfiguration implements AfterAllCallback, BeforeAllCallback {

    private static final Logger log = LoggerFactory.getLogger(TestContainerConfiguration.class);
    private static final String REDIS_IMAGE = "redis:7.0.8-alpine";
    private static final int REDIS_PORT = 6379;
    private GenericContainer<?> genericContainer;

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        try {
            genericContainer = new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE))
                .withExposedPorts(REDIS_PORT);
            System.setProperty("spring.data.redis.host", genericContainer.getHost());
            System.setProperty("spring.data.redis.port", String.valueOf(genericContainer.getMappedPort(REDIS_PORT)));
        } catch (Exception ex) {
            log.error("message: {}", ex.getMessage());
        }
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        if (genericContainer != null) {
            genericContainer.stop();
        }
    }
}
