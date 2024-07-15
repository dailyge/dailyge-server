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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import org.springframework.restdocs.restassured.RestAssuredOperationPreprocessorsConfigurer;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public final class RestAssureConfig {

    private RestAssureConfig() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static ObjectMapper initObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        RestAssured.config = new RestAssuredConfig().objectMapperConfig(
            new ObjectMapperConfig().jackson2ObjectMapperFactory((clazz, charset) -> objectMapper)
        );
        return objectMapper;
    }

    public static RequestSpecification initSpecificationConfig(
        final RestDocumentationContextProvider restDocumentation,
        final int port
    ) {
        final String schema;
        final String env = System.getenv("ENV");
        if (env != null && env.equals("prod")) {
            schema = "api.dailyge.com";
        } else {
            schema = "api-dev.dailyge.com";
        }
        final OperationPreprocessor operationPreprocessor = modifyUris()
            .host(schema)
            .removePort();

        final RestAssuredOperationPreprocessorsConfigurer restDocumentationFilter =
            documentationConfiguration(restDocumentation)
                .operationPreprocessors()
                .withRequestDefaults(operationPreprocessor, prettyPrint())
                .withResponseDefaults(prettyPrint());

        return new RequestSpecBuilder()
            .setPort(port)
            .addFilter(restDocumentationFilter)
            .build();
    }

    public static void test() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
//        RestAssured.baseURI = "https://your-secure-api.com";
//
//        RestAssured.config = RestAssured.config().sslConfig(
//            io.restassured.config.SSLConfig.sslConfig()
//                .trustStore("path/to/your_truststore.jks", "your_password")
//        );
        RestAssured.baseURI = "https://www.dailyge.com";
    }
}
