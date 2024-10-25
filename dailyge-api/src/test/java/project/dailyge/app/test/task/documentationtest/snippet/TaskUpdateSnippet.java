package project.dailyge.app.test.task.documentationtest.snippet;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import static project.dailyge.app.common.CommonSnippet.COOKIE_HEADER_DESCRIPTORS;

import java.util.Arrays;
import java.util.List;

public final class TaskUpdateSnippet implements TaskSnippet {

    private static final String TASK_UPDATE_SUMMARY = "Task 수정 API";
    private static final String TASK_STATUS_UPDATE_SUMMARY = "Task 상태 수정 API";
    private static final String TASK_UPDATE_DESCRIPTION = "Task를 수정합니다.";
    private static final String TASK_STATUS_UPDATE_DESCRIPTION = "Task 상태를 수정합니다.";

    private TaskUpdateSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static RestDocumentationFilter createTaskUpdateFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(COOKIE_HEADER_DESCRIPTORS)
                .requestFields(TASK_UPDATE_REQUEST_FIELDS)
                .responseFields(TASK_UPDATE_RESPONSE_FIELD_DESCRIPTOR)
                .tag(TAG)
                .summary(TASK_UPDATE_SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(TASK_UPDATE_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(TASK_TOKEN_COOKIE_DESCRIPTORS),
                    requestFields(Arrays.stream(TASK_UPDATE_REQUEST_FIELDS).toList()),
                    responseFields(Arrays.stream(TASK_UPDATE_RESPONSE_FIELD_DESCRIPTOR).toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE).toList())
                );
            }
        );
    }

    // 상태 수정
    public static RestDocumentationFilter createTaskStatusUpdateFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestFields(TASK_STATUS_UPDATE_REQUEST_FIELDS)
                .responseFields(TASK_STATUS_UPDATE_RESPONSE)
                .tag(TAG)
                .summary(TASK_STATUS_UPDATE_SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(TASK_STATUS_UPDATE_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(TASK_TOKEN_COOKIE_DESCRIPTORS),
                    requestFields(Arrays.stream(TASK_STATUS_UPDATE_REQUEST_FIELDS).toList()),
                    responseFields(Arrays.stream(TASK_STATUS_UPDATE_RESPONSE).toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE).toList())
                );
            }
        );
    }
}
