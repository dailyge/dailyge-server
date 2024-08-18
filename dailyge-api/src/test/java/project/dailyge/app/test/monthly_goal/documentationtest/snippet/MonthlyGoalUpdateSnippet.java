package project.dailyge.app.test.monthly_goal.documentationtest.snippet;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import org.springframework.restdocs.restassured.RestDocumentationFilter;

import java.util.Arrays;
import java.util.List;

public final class MonthlyGoalUpdateSnippet implements MonthlyGoalSnippet {

    private static final String SUMMARY = "월간 목표 수정 API";
    private static final String DESCRIPTION = "월간 목표를 수정합니다.";

    private MonthlyGoalUpdateSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static RestDocumentationFilter updateMonthlyGoalFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(HEADER_DESCRIPTOR)
                .pathParameters(MONTHLY_GOAL_ID_PATH_PARAMETER_DESCRIPTORS)
                .requestFields(MONTHLY_GOAL_UPDATE_REQUEST_FIELDS_DESCRIPTORS)
                .responseFields(MONTHLY_GOAL_UPDATE_RESPONSE_FIELD_DESCRIPTORS)
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
                    pathParameters(Arrays.stream(MONTHLY_GOAL_ID_PATH_PARAMETER_DESCRIPTORS).toList()),
                    requestFields(MONTHLY_GOAL_UPDATE_REQUEST_FIELDS_DESCRIPTORS),
                    responseFields(MONTHLY_GOAL_UPDATE_RESPONSE_FIELD_DESCRIPTORS)
                );
            }
        );
    }

    public static RestDocumentationFilter updateMonthlyGoalStatusFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(HEADER_DESCRIPTOR)
                .pathParameters(MONTHLY_GOAL_ID_PATH_PARAMETER_DESCRIPTORS)
                .requestFields(MONTHLY_GOAL_STATUS_UPDATE_REQUEST_FIELDS_DESCRIPTORS)
                .responseFields(MONTHLY_GOAL_UPDATE_RESPONSE_FIELD_DESCRIPTORS)
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
                    pathParameters(Arrays.stream(MONTHLY_GOAL_ID_PATH_PARAMETER_DESCRIPTORS).toList()),
                    requestFields(MONTHLY_GOAL_STATUS_UPDATE_REQUEST_FIELDS_DESCRIPTORS),
                    responseFields(MONTHLY_GOAL_UPDATE_RESPONSE_FIELD_DESCRIPTORS)
                );
            }
        );
    }

    public static RestDocumentationFilter updateMonthlyGoalErrorFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(HEADER_DESCRIPTOR)
                .pathParameters(MONTHLY_GOAL_ID_PATH_PARAMETER_DESCRIPTORS)
                .requestFields(MONTHLY_GOAL_UPDATE_REQUEST_FIELDS_DESCRIPTORS)
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
                    pathParameters(Arrays.stream(MONTHLY_GOAL_ID_PATH_PARAMETER_DESCRIPTORS).toList()),
                    requestFields(MONTHLY_GOAL_UPDATE_REQUEST_FIELDS_DESCRIPTORS),
                    responseFields(ERROR_RESPONSE_FIELD_DESCRIPTORS)
                );
            }
        );
    }
}
