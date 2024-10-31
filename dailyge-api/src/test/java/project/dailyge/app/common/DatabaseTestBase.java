package project.dailyge.app.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
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
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.DailygeApplication;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.user.application.UserWriteService;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheWriteService;
import project.dailyge.entity.user.UserJpaEntity;

import java.time.LocalDate;

import static project.dailyge.app.common.RestAssureConfig.initObjectMapper;
import static project.dailyge.app.common.RestAssureConfig.initSpecificationConfig;
import static project.dailyge.app.test.user.fixture.UserFixture.EMAIL;
import static project.dailyge.app.test.user.fixture.UserFixture.createUser;
import static project.dailyge.entity.user.Role.NORMAL;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
@ExtendWith(RestDocumentationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = DailygeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class DatabaseTestBase {

    @Value("${jwt.access-token}")
    protected String accessToken;

    protected static final String IDENTIFIER = "{class_name}/{method_name}";
    protected static final String USER_ID_KEY = "dailyge_user_id";

    @LocalServerPort
    protected int port;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private DatabaseInitializer databaseInitialization;

    @Autowired
    private UserWriteService userWriteService;

    @Autowired
    private UserCacheWriteService userCacheWriteService;

    protected RequestSpecification specification;
    protected ObjectMapper objectMapper;
    protected UserJpaEntity newUser;
    protected DailygeUser dailygeUser;
    protected final DailygeUser invalidUser = new DailygeUser(Long.MAX_VALUE, NORMAL);
    protected LocalDate now;

    protected DatabaseTestBase() {
        this.objectMapper = initObjectMapper();
    }

    @DynamicPropertySource
    public static void overrideProps(final DynamicPropertyRegistry registry) {
        TestContainerConfig.overrideProps(registry);
    }

    @BeforeEach
    @Transactional
    void setUp(final RestDocumentationContextProvider restDocumentation) {
        databaseInitialization.initData();
        persist(createUser(userWriteService.save(EMAIL)));
        this.specification = initSpecificationConfig(restDocumentation, port);
    }

    @AfterEach
    protected void afterEach() {
        RestAssured.reset();
    }

    protected Cookie getAccessTokenCookie() {
        return new Cookie.Builder("Access-Token", accessToken).build();
    }

    @Transactional
    protected void persist(final UserJpaEntity user) {
        userWriteService.save(user);
        newUser = user;
        final UserCache userCache = new UserCache(
            user.getId(),
            user.getNickname(),
            user.getEmail(),
            user.getProfileImageUrl(),
            user.getRoleAsString()
        );
        userCacheWriteService.save(userCache);
        dailygeUser = new DailygeUser(user.getId(), user.getRole());
    }

    protected Cookie getCouponCookie() {
        return new Cookie.Builder("isParticipated", "true")
            .build();
    }
}
