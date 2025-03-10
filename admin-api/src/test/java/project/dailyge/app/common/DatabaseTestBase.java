package project.dailyge.app.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.DailygeAdminApplication;
import static project.dailyge.app.common.RestAssureConfig.initObjectMapper;
import static project.dailyge.app.common.RestAssureConfig.initSpecificationConfig;
import project.dailyge.app.common.auth.DailygeUser;
import static project.dailyge.app.test.user.fixture.UserFixture.createUser;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheWriteService;
import static project.dailyge.entity.user.Role.NORMAL;
import project.dailyge.entity.user.UserJpaEntity;

import java.time.LocalDate;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(classes = DailygeAdminApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
        persist(createUser(1L));
        this.specification = initSpecificationConfig(restDocumentation, port);
    }

    @AfterEach
    void afterEach() {
        RestAssured.reset();
    }

    protected Cookie getAccessTokenCookie() {
        return new Cookie.Builder("dg_sess", accessToken).build();
    }

    protected UserJpaEntity persist(final UserJpaEntity user) {
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
        return user;
    }
}
