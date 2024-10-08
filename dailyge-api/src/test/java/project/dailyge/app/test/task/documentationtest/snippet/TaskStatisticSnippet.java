package project.dailyge.app.test.task.documentationtest.snippet;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import static project.dailyge.app.common.CommonSnippet.COOKIE_HEADER_DESCRIPTORS;

import java.util.Arrays;
import java.util.List;

public final class TaskStatisticSnippet implements TaskSnippet {

    // Summary
    private static final String WEEKLY_TASKS_STATISTIC_SUMMARY = "Task 주간 달성률 조회 API";
    private static final String MONTHLY_TASKS_STATISTIC_SUMMARY = "Task 월간 달성률 조회 API";

    // Description
    private static final String WEEKLY_TASK_DETAIL_SEARCH_DESCRIPTION = "Tasks 주간 달성률을 조회하는 API 입니다.";
    private static final String MONTHLY_TASK_DETAIL_SEARCH_DESCRIPTION = "Tasks 주간 달성률을 조회하는 API 입니다.";

    private TaskStatisticSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    /**
     * 주간 Tasks 달성률 조회 문서
     */
    public static RestDocumentationFilter createWeeklyTasksStatisticSearchFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(COOKIE_HEADER_DESCRIPTORS)
                .pathParameters(TASK_ID_PATH_PARAMETER_DESCRIPTORS)
                .queryParameters(WEEKLY_TASKS_STATISTIC_DATE_QUERY_PARAMETER_DESCRIPTORS)
                .responseFields(WEEKLY_TASKS_STATISTIC_RESPONSE_FIELD_DESCRIPTOR)
                .tag(TAG)
                .summary(WEEKLY_TASKS_STATISTIC_SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(WEEKLY_TASK_DETAIL_SEARCH_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(TASK_TOKEN_COOKIE_DESCRIPTORS),
                    queryParameters(WEEKLY_TASKS_STATISTIC_DATE_QUERY_PARAMETER_DESCRIPTORS),
                    responseFields(Arrays.stream(WEEKLY_TASKS_STATISTIC_RESPONSE_FIELD_DESCRIPTOR).toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE).toList())
                );
            }
        );
    }

    /**
     * 월간 Tasks 달성률 조회 문서
     */
    public static RestDocumentationFilter createMonthlyTasksStatisticSearchFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .pathParameters(TASK_ID_PATH_PARAMETER_DESCRIPTORS)
                .queryParameters(MONTHLY_TASKS_STATISTIC_DATE_QUERY_PARAMETER_DESCRIPTORS)
                .responseFields(MONTHLY_TASKS_STATISTIC_RESPONSE_FIELD_DESCRIPTOR)
                .tag(TAG)
                .summary(MONTHLY_TASKS_STATISTIC_SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(MONTHLY_TASK_DETAIL_SEARCH_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(TASK_TOKEN_COOKIE_DESCRIPTORS),
                    queryParameters(MONTHLY_TASKS_STATISTIC_DATE_QUERY_PARAMETER_DESCRIPTORS),
                    responseFields(Arrays.stream(MONTHLY_TASKS_STATISTIC_RESPONSE_FIELD_DESCRIPTOR).toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE).toList())
                );
            }
        );
    }

    /**
     * 월간 주차 별 Tasks 달성률 조회 문서
     */
    public static RestDocumentationFilter createMonthlyWeekTasksStatisticSearchFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .pathParameters(TASK_ID_PATH_PARAMETER_DESCRIPTORS)
                .queryParameters(MONTHLY_WEEK_TASKS_STATISTIC_DATE_QUERY_PARAMETER_DESCRIPTORS)
                .responseFields(MONTHLY_WEEK_TASKS_STATISTIC_RESPONSE_FIELD_DESCRIPTOR)
                .tag(TAG)
                .summary(MONTHLY_TASKS_STATISTIC_SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(MONTHLY_TASK_DETAIL_SEARCH_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(TASK_TOKEN_COOKIE_DESCRIPTORS),
                    queryParameters(MONTHLY_WEEK_TASKS_STATISTIC_DATE_QUERY_PARAMETER_DESCRIPTORS),
                    responseFields(Arrays.stream(MONTHLY_WEEK_TASKS_STATISTIC_RESPONSE_FIELD_DESCRIPTOR).toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE).toList())
                );
            }
        );
    }
}
