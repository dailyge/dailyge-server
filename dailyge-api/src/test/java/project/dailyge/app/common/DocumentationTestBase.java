package project.dailyge.app.common;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import io.restassured.*;
import io.restassured.builder.*;
import io.restassured.config.*;
import io.restassured.specification.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.restdocs.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.web.server.*;
import org.springframework.restdocs.*;
import org.springframework.restdocs.operation.preprocess.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import org.springframework.restdocs.restassured.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.*;
import org.springframework.test.web.servlet.*;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class DocumentationTestBase extends DatabaseTestBase {

    private static final String SCHEMA = "www.study-with-me.com";

    @LocalServerPort
    protected int port;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private DatabaseInitializer databaseInitialization;

    protected RequestSpecification specification;

    protected DocumentationTestBase() {
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
