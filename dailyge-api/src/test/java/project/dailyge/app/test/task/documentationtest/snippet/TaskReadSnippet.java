package project.dailyge.app.test.task.documentationtest.snippet;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import org.springframework.restdocs.restassured.RestDocumentationFilter;

import java.util.Arrays;
import java.util.List;

public final class TaskReadSnippet implements TaskSnippet {

    // Summary
    private static final String TASK_STATUS_LIST_SUMMARY = "Task 상태 목록 조회 API";
    private static final String TASK_DETAIL_SEARCH_SUMMARY = "Task 상세 조회 API";
    private static final String MONTHLY_TASK_ID_SEARCH_SUMMARY = "월간 일정표 ID 조회 API";
    private static final String TASKS_SEARCH_SUMMARY = "월간 일정 목록 조회 API";
    private static final String MONTHLY_TASK_DETAIL_SEARCH_V2_SUMMARY = "월간 일정표 상세 조회 API(V2)";

    // Description
    private static final String TASK_STATUS_LIST_DESCRIPTION = "Task 상태 목록(TODO, IN_PROGRESS, DONE)을 조회하는 API 입니다.";
    private static final String TASK_DETAIL_SEARCH_DESCRIPTION = "Task 상세 정보를 조회하는 API 입니다.";
    private static final String MONTHLY_TASK_ID_SEARCH_DESCRIPTION = "월간 일정 ID를 조회하는 API 입니다.";
    private static final String TASKS_SEARCH_DESCRIPTION = "월간 일정을 조회하는 API 입니다.";
    private static final String MONTHLY_TASK_DETAIL_SEARCH_V2_DESCRIPTION = "월간 일정 정보를 조회하는 API 입니다. 사용자 ID와 날짜로 월간 일정표를 조회합니다.";

    private TaskReadSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    /**
     * Task 상세 조회 문서
     */
    public static RestDocumentationFilter createTaskDetailSearchFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(HEADER_DESCRIPTOR)
                .pathParameters(TASK_ID_PATH_PARAMETER_DESCRIPTORS)
                .queryParameters(DATE_QUERY_PARAMETER_DESCRIPTORS)
                .responseFields(TASK_DETAIL_SEARCH_RESPONSE_FIELD_DESCRIPTOR)
                .tag(TAG)
                .summary(TASK_DETAIL_SEARCH_SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(TASK_DETAIL_SEARCH_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestHeaders(List.of(HEADER_DESCRIPTOR)),
                    pathParameters(TASK_ID_PATH_PARAMETER_DESCRIPTORS),
                    queryParameters(DATE_QUERY_PARAMETER_DESCRIPTORS),
                    responseFields(Arrays.stream(TASK_DETAIL_SEARCH_RESPONSE_FIELD_DESCRIPTOR).toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE).toList())
                );
            }
        );
    }

    /**
     * MonthlyTask ID 조회 문서
     */
    public static RestDocumentationFilter createMonthlyTaskIdSearchFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(HEADER_DESCRIPTOR)
                .pathParameters(DATE_QUERY_PARAMETER_DESCRIPTORS)
                .responseFields(MONTHLY_TASK_ID_READ_RESPONSE_FIELD_DESCRIPTOR)
                .tag(TAG)
                .summary(MONTHLY_TASK_ID_SEARCH_SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(MONTHLY_TASK_ID_SEARCH_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestHeaders(List.of(HEADER_DESCRIPTOR)),
                    pathParameters(DATE_QUERY_PARAMETER_DESCRIPTORS),
                    responseFields(Arrays.stream(MONTHLY_TASK_ID_READ_RESPONSE_FIELD_DESCRIPTOR).toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE).toList())
                );
            }
        );
    }

    /**
     * MonthlyTask 상세 조회 문서
     */
    public static RestDocumentationFilter createMonthlyTasksSearchWithIdFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(HEADER_DESCRIPTOR)
                .pathParameters(DATE_QUERY_PARAMETER_DESCRIPTORS)
                .responseFields(MONTHLY_TASK_READ_RESPONSE_FIELD_DESCRIPTOR)
                .tag(TAG)
                .summary(TASKS_SEARCH_SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(TASKS_SEARCH_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestHeaders(List.of(HEADER_DESCRIPTOR)),
                    pathParameters(DATE_QUERY_PARAMETER_DESCRIPTORS),
                    responseFields(Arrays.stream(MONTHLY_TASK_READ_RESPONSE_FIELD_DESCRIPTOR).toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE).toList())
                );
            }
        );
    }

    /**
     * Task 상태 목록 조회 문서
     */
    public static RestDocumentationFilter createTaskStatusesReadFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(HEADER_DESCRIPTOR)
                .responseFields(TASK_STATUS_LIST_READ_RESPONSE_FIELD_DESCRIPTOR)
                .tag(TAG)
                .summary(TASK_STATUS_LIST_SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(TASK_STATUS_LIST_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestHeaders(List.of(HEADER_DESCRIPTOR)),
                    responseFields(Arrays.stream(TASK_STATUS_LIST_READ_RESPONSE_FIELD_DESCRIPTOR).toList())
                );
            }
        );
    }
}
