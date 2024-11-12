package project.dailyge.app.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
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
import static project.dailyge.app.common.RestAssureConfig.initObjectMapper;
import static project.dailyge.app.common.RestAssureConfig.initSpecificationConfig;
import project.dailyge.app.core.common.auth.DailygeUser;
import project.dailyge.app.core.user.application.UserWriteService;
import project.dailyge.app.core.user.persistence.UserWriteDao;
import static project.dailyge.app.test.user.fixture.UserFixture.EMAIL;
import static project.dailyge.app.test.user.fixture.UserFixture.NICKNAME;
import static project.dailyge.app.test.user.fixture.UserFixture.createUser;
import project.dailyge.core.cache.user.UserCache;
import project.dailyge.core.cache.user.UserCacheWriteService;
import static project.dailyge.entity.user.Role.NORMAL;
import project.dailyge.entity.user.UserJpaEntity;

import java.time.LocalDate;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
@Execution(ExecutionMode.SAME_THREAD)
@ExtendWith(RestDocumentationExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@SpringBootTest(classes = DailygeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class DatabaseTestBase {

    @Value("${jwt.access-token}")
    protected String accessToken;

    @Value("${jwt.receiver-access-token}")
    protected String noteReceiverAccessToken;

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
    private UserWriteDao userWriteDao;

    @Autowired
    private UserCacheWriteService userCacheWriteService;

    protected RequestSpecification specification;
    protected ObjectMapper objectMapper;
    protected UserJpaEntity newUser;
    protected UserJpaEntity noteReceivedDailygeUser;
    protected DailygeUser dailygeUser;
    protected DailygeUser receivedDailygeUser;
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
        initBasicUser();
        initNoteReceivedUser();
        this.specification = initSpecificationConfig(restDocumentation, port);
    }

    @Transactional
    public void initBasicUser() {
        newUser = persist(createUser(null, NICKNAME, EMAIL));
        this.dailygeUser = new DailygeUser(newUser.getId(), NORMAL);
    }

    @Transactional
    public void initNoteReceivedUser() {
        noteReceivedDailygeUser = save(new UserJpaEntity(300L, "kmularise", "kmularise@gmail.com"));
        this.receivedDailygeUser = new DailygeUser(noteReceivedDailygeUser.getId(), NORMAL);
    }

    @AfterEach
    protected void afterEach() {
        RestAssured.reset();
    }

    protected Cookie getAccessTokenCookie() {
        return new Cookie.Builder("dg_sess", accessToken)
            .build();
    }

    protected Cookie getNoteReceiverAccessTokenCookie() {
        return new Cookie.Builder("dg_sess", noteReceiverAccessToken)
            .build();
    }

    protected Cookie getCouponCookie() {
        return new Cookie.Builder("isParticipated", "true")
            .build();
    }

    @Transactional
    protected UserJpaEntity persist(final UserJpaEntity user) {
        userWriteService.save(user);
        final UserCache userCache = new UserCache(
            user.getId(),
            user.getNickname(),
            user.getEmail(),
            user.getProfileImageUrl(),
            user.getRoleAsString()
        );
        userCacheWriteService.save(userCache);
        return user;
    }

    @Transactional
    protected UserJpaEntity save(final UserJpaEntity user) {
        final UserJpaEntity newUser = userWriteDao.insertUser(user.getId(), user.getEmail(), user.getNickname());
        final UserCache userCache = new UserCache(
            newUser.getId(),
            user.getNickname(),
            user.getEmail(),
            user.getProfileImageUrl(),
            user.getRoleAsString()
        );
        userCacheWriteService.save(userCache);
        return user;
    }
}
