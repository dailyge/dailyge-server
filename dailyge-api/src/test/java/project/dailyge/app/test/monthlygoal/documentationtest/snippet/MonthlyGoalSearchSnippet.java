package project.dailyge.app.test.monthlygoal.documentationtest.snippet;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import org.springframework.restdocs.restassured.RestDocumentationFilter;

import java.util.Arrays;
import java.util.List;

public final class MonthlyGoalSearchSnippet implements MonthlyGoalSnippet {

    private static final String SUMMARY = "월간 목표 조회 API";
    private static final String DESCRIPTION = "월간 목표 목록을 조회합니다.";

    private MonthlyGoalSearchSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static RestDocumentationFilter createMonthlyGoalSearchFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .pathParameters(MONTHLY_GOAL_CURSOR_PAGING_PARAMETER_DESCRIPTORS)
                .responseFields(MONTHLY_GOAL_PAGING_RESPONSE_FIELD_DESCRIPTORS)
                .tag(tag)
                .summary(SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(MONTHLY_GOAL_TOKEN_COOKIE_DESCRIPTORS),
                    pathParameters(Arrays.stream(MONTHLY_GOAL_CURSOR_PAGING_PARAMETER_DESCRIPTORS).toList()),
                    responseFields(MONTHLY_GOAL_PAGING_RESPONSE_FIELD_DESCRIPTORS)
                );
            }
        );
    }

    public static RestDocumentationFilter deleteMonthlyGoalErrorFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .pathParameters(MONTHLY_GOAL_ID_PATH_PARAMETER_DESCRIPTORS)
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
                    requestCookies(MONTHLY_GOAL_TOKEN_COOKIE_DESCRIPTORS),
                    pathParameters(Arrays.stream(MONTHLY_GOAL_ID_PATH_PARAMETER_DESCRIPTORS).toList()),
                    responseFields(ERROR_RESPONSE_FIELD_DESCRIPTORS)
                );
            }
        );
    }
}
