package project.dailyge.app.test.task.documentationtest.snippet;

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

public final class TaskCreateSnippet implements TaskSnippet {

    private static final String SUMMARY = "월간 일정표 생성 API";
    private static final String DESCRIPTION = "월간 일정표를 생성합니다. 월간 일정표가 이미 존재한다면 이를 중복생성할 수 없습니다.";

    private TaskCreateSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static RestDocumentationFilter createTasksFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(HEADER_DESCRIPTOR)
                .requestFields(TASK_CREATE_REQUEST_FIELDS)
                .responseFields(TASK_CREATE_RESPONSE)
                .tag(TAG)
                .summary(SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestHeaders(List.of(HEADER_DESCRIPTOR)),
                    requestFields(Arrays.stream(TASK_CREATE_REQUEST_FIELDS).toList()),
                    responseFields(Arrays.stream(TASK_CREATE_RESPONSE).toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE).toList())
                );
            }
        );
    }

    public static RestDocumentationFilter createMonthlyTasksFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(HEADER_DESCRIPTOR)
                .requestFields(MONTHLY_TASK_CREATE_REQUEST_FIELDS)
                .responseFields(MONTHLY_TASK_CREATE_RESPONSE)
                .tag(TAG)
                .summary(SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestHeaders(List.of(HEADER_DESCRIPTOR)),
                    requestFields(Arrays.stream(MONTHLY_TASK_CREATE_REQUEST_FIELDS).toList()),
                    responseFields(Arrays.stream(MONTHLY_TASK_CREATE_RESPONSE).toList())
                );
            }
        );
    }

    public static RestDocumentationFilter createMonthlyTasksErrorFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(HEADER_DESCRIPTOR)
                .requestFields(MONTHLY_TASK_CREATE_REQUEST_FIELDS)
                .responseFields(ERROR_RESPONSE)
                .tag(TAG)
                .summary(SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestHeaders(List.of(HEADER_DESCRIPTOR)),
                    requestFields(Arrays.stream(MONTHLY_TASK_CREATE_REQUEST_FIELDS).toList()),
                    responseFields(Arrays.stream(MONTHLY_TASK_CREATE_RESPONSE).toList())
                );
            }
        );
    }
}
