package project.dailyge.app.common;

import static java.util.concurrent.CompletableFuture.runAsync;
import org.junit.jupiter.api.AfterAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import static org.testcontainers.containers.wait.strategy.Wait.forListeningPort;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @AfterAll을 통해 모든 테스트가 끝난 후, 자원을 해제하기 때문에
 * @SuppressWarnings("resource")로 불필요한 경고 제거.
 */
public final class TestContainerConfig {

    private TestContainerConfig() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    private static final String REDIS_IMAGE = "redis:7.0.8-alpine";
    private static final String MONGO_IMAGE = "mongo:6.0";
    private static final String LOCAL_STACK_IMAGE = "localstack/localstack:latest";
    private static final String DEFAULT_REGION = "ap-northeast-2";

    @Container
    @SuppressWarnings("resource")
    private static final GenericContainer<?> redisContainer = new GenericContainer<>(REDIS_IMAGE)
        .withExposedPorts(6379)
        .withReuse(true)
        .waitingFor(forListeningPort())
        .withLabel("reuse.UUID", UUID.randomUUID().toString())
        .withCreateContainerCmdModifier(cmd -> {
            Objects.requireNonNull(cmd.getHostConfig())
                .withCpuCount(1L)
                .withMemory(2147483648L);
        });

    @Container
    @SuppressWarnings("resource")
    private static final GenericContainer<?> mongoContainer = new GenericContainer<>(MONGO_IMAGE)
        .withExposedPorts(27017)
        .withReuse(true)
        .waitingFor(forListeningPort())
        .withLabel("reuse.UUID", UUID.randomUUID().toString())
        .withEnv("MONGO_INITDB_ROOT_USERNAME", "dailyge")
        .withEnv("MONGO_INITDB_ROOT_PASSWORD", "dailyge")
        .withCommand("--bind_ip_all", "--nojournal")
        .withCreateContainerCmdModifier(cmd -> {
            Objects.requireNonNull(cmd.getHostConfig())
                .withCpuCount(1L)
                .withMemory(2147483648L);
        });

    @Container
    private static final LocalStackContainer localStackContainer = new LocalStackContainer(DockerImageName.parse(LOCAL_STACK_IMAGE))
        .withServices(LocalStackContainer.Service.SNS, LocalStackContainer.Service.SQS)
        .withEnv("AWS_DEFAULT_REGION", DEFAULT_REGION)
        .withExposedPorts(4566);

    @DynamicPropertySource
    public static void overrideProps(final DynamicPropertyRegistry registry) {
        try {
            final CompletableFuture<Void> redisPropertiesSetup = runAsync(() -> initMemoryDbProperties(registry));
            final CompletableFuture<Void> mongoPropertiesSetup = runAsync(() -> initDocumentDbProperties(registry));
            final CompletableFuture<Void> localStackPropertiesSetup = runAsync(() -> initLocalStack(registry));
            CompletableFuture.allOf(redisPropertiesSetup, mongoPropertiesSetup, localStackPropertiesSetup).join();
        } catch (Exception ex) {
            throw new RuntimeException("Error initializing containers.", ex);
        }
    }

    private static void initMemoryDbProperties(final DynamicPropertyRegistry registry) {
        redisContainer.start();
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort);
    }

    private static void initDocumentDbProperties(final DynamicPropertyRegistry registry) {
        mongoContainer.start();

        final String mongoConnectionString = String.format(
            "mongodb://%s:%s@%s:%d/dailyge?authSource=admin",
            "dailyge", "dailyge", mongoContainer.getHost(), mongoContainer.getMappedPort(27017)
        );
        System.setProperty("mongodb.container.port", mongoContainer.getMappedPort(27017).toString());
        System.setProperty("mongodb.container.host", mongoContainer.getHost());
        System.setProperty("mongodb.container.connectionString", mongoConnectionString);
        registry.add("spring.data.mongodb.uri", () -> mongoConnectionString);
    }

    private static void initLocalStack(final DynamicPropertyRegistry registry) {
        localStackContainer.start();

        final SqsClient sqsClient = SqsClient.builder()
            .endpointOverride(localStackContainer.getEndpointOverride(LocalStackContainer.Service.SQS))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(localStackContainer.getAccessKey(), localStackContainer.getSecretKey())))
            .region(Region.of(DEFAULT_REGION))
            .build();
        final CreateQueueResponse createQueueResponse = sqsClient.createQueue(CreateQueueRequest.builder()
            .queueName("dailyge-event-queue")
            .build());
        final String queueUrl = createQueueResponse.queueUrl();

        registry.add("spring.cloud.aws.region.static", () -> DEFAULT_REGION);
        registry.add("spring.cloud.aws.credentials.access-key", localStackContainer::getAccessKey);
        registry.add("spring.cloud.aws.credentials.secret-key", localStackContainer::getSecretKey);
        registry.add("application.amazon.sqs.queue-url", () -> queueUrl);
    }

    @AfterAll
    static void releaseResource() {
        if (redisContainer != null) {
            redisContainer.stop();
        }
        if (mongoContainer != null) {
            mongoContainer.stop();
        }
    }
}
