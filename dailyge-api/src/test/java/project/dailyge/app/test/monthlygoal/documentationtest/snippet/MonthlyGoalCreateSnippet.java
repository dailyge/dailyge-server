package project.dailyge.app.test.monthlygoal.documentationtest.snippet;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import org.springframework.restdocs.restassured.RestDocumentationFilter;

import java.util.Arrays;
import java.util.List;

public final class MonthlyGoalCreateSnippet implements MonthlyGoalSnippet {

    private static final String SUMMARY = "월간 목표 생성 API";
    private static final String DESCRIPTION = "월간 목표를 생성합니다.";

    private MonthlyGoalCreateSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static RestDocumentationFilter createMonthlyGoalFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(HEADER_DESCRIPTOR)
                .requestFields(MONTHLY_GOAL_CREATE_REQUEST_FIELDS_DESCRIPTORS)
                .responseFields(MONTHLY_GOAL_CREATE_RESPONSE_FIELD_DESCRIPTORS)
                .tag(tag)
                .summary(SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestHeaders(List.of(HEADER_DESCRIPTOR)),
                    requestFields(Arrays.stream(MONTHLY_GOAL_CREATE_REQUEST_FIELDS_DESCRIPTORS).toList()),
                    responseFields(Arrays.stream(MONTHLY_GOAL_CREATE_RESPONSE_FIELD_DESCRIPTORS).toList())
                );
            }
        );
    }

    public static RestDocumentationFilter createMonthlyGoalErrorFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(HEADER_DESCRIPTOR)
                .requestFields(MONTHLY_GOAL_CREATE_REQUEST_FIELDS_DESCRIPTORS)
                .responseFields(ERROR_RESPONSE_FIELD_DESCRIPTORS)
                .tag(tag)
                .summary(SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestHeaders(List.of(HEADER_DESCRIPTOR)),
                    requestFields(Arrays.stream(MONTHLY_GOAL_CREATE_REQUEST_FIELDS_DESCRIPTORS).toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE_FIELD_DESCRIPTORS).toList())
                );
            }
        );
    }
}
