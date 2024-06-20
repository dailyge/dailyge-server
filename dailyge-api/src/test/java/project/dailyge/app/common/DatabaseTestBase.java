package project.dailyge.app.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import org.springframework.restdocs.restassured.RestAssuredOperationPreprocessorsConfigurer;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
@ExtendWith(TestContainerConfiguration.class)
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class DatabaseTestBase {

    @Value("${jwt.access-token}")
    protected String accessToken;
    private static final String SCHEMA = "www.dailyge.com";
    protected static final String IDENTIFIER = "{class_name}/{method_name}";
    protected static final String USER_ID_KEY = "dailyge_user_id";
    protected static final String AUTHORIZATION = "Authorization";

    @LocalServerPort
    protected int port;

    @Autowired
    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper;

    @Autowired
    private DatabaseInitializer databaseInitialization;

    protected RequestSpecification specification;

    protected DatabaseTestBase() {
        initRestAssureConfiguration();
    }

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        databaseInitialization.truncateAllEntity();

        initRestAssureConfiguration();

        OperationPreprocessor operationPreprocessor = modifyUris()
            .host(SCHEMA)
            .removePort();

        RestAssuredOperationPreprocessorsConfigurer restDocumentationFilter =
            documentationConfiguration(restDocumentation)
                .operationPreprocessors()
                .withRequestDefaults(operationPreprocessor, prettyPrint())
                .withResponseDefaults(prettyPrint());

        this.specification = new RequestSpecBuilder()
            .setPort(port)
            .addFilter(restDocumentationFilter)
            .build();
    }

    private void initRestAssureConfiguration() {
        objectMapper = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        RestAssured.config = new RestAssuredConfig().objectMapperConfig(
            new ObjectMapperConfig().jackson2ObjectMapperFactory((clazz, charset) -> objectMapper)
        );
    }

    @AfterEach
    void afterEach() {
        RestAssured.reset();
    }
}
