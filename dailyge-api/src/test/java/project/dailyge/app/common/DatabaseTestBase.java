package project.dailyge.app.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import project.dailyge.app.DailygeApplication;
import static project.dailyge.app.common.RestAssureConfig.initObjectMapper;
import static project.dailyge.app.common.RestAssureConfig.initSpecificationConfig;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.user.application.UserWriteUseCase;
import project.dailyge.entity.user.UserJpaEntity;

import java.time.LocalDate;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(classes = DailygeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class DatabaseTestBase {

    @Value("${jwt.access-token}")
    protected String accessToken;

    protected static final String IDENTIFIER = "{class_name}/{method_name}";
    protected static final String USER_ID_KEY = "dailyge_user_id";
    protected static final String AUTHORIZATION = "Authorization";

    @LocalServerPort
    protected int port;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private DatabaseInitializer databaseInitialization;

    @Autowired
    protected UserWriteUseCase userWriteUseCase;

    protected RequestSpecification specification;
    protected ObjectMapper objectMapper;
    protected UserJpaEntity newUser;
    protected DailygeUser dailygeUser;
    protected LocalDate now;

    protected DatabaseTestBase() {
        this.objectMapper = initObjectMapper();
    }

    @DynamicPropertySource
    public static void overrideProps(final DynamicPropertyRegistry registry) {
        TestContainerConfig.overrideProps(registry);
    }

    @BeforeEach
    void setUp(final RestDocumentationContextProvider restDocumentation) {
        databaseInitialization.initData();
        this.specification = initSpecificationConfig(restDocumentation, port);
    }

    @AfterEach
    void afterEach() {
        RestAssured.reset();
    }

    protected String getAuthorizationHeader() {
        return "Bearer " + accessToken;
    }
}
