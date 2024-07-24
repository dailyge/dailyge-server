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

public final class RestAssureConfig {

    private RestAssureConfig() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static ObjectMapper initObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper()
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
        final String env = System.getenv("ENV");
        final String scheme = "https";
        final String host;
        if (env != null && env.equals("prod")) {
            host = "api.dailyge.com";
        } else {
            host = "api-dev.dailyge.com";
        }

        final OperationPreprocessor operationPreprocessor = modifyUris()
            .scheme(scheme)
            .host(host)
            .removePort();

        final RestAssuredOperationPreprocessorsConfigurer restDocumentationFilter =
            documentationConfiguration(restDocumentation)
                .operationPreprocessors()
                .withRequestDefaults(operationPreprocessor, prettyPrint())
                .withResponseDefaults(operationPreprocessor, prettyPrint());
        return new RequestSpecBuilder()
            .setPort(port)
            .addFilter(restDocumentationFilter)
            .build();
    }
}
