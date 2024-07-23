package project.dailyge.app.test.task.documentationtest.snippet;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import com.epages.restdocs.apispec.Schema;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import org.springframework.restdocs.restassured.RestDocumentationFilter;

import java.util.Arrays;
import java.util.List;

public final class TaskUpdateSnippet implements TaskSnippet {

    private static final String TASK_UPDATE_SUMMARY = "Task 수정 API";
    private static final String TASK_STATUS_UPDATE_SUMMARY = "Task 수정 API";
    private static final String TASK_UPDATE_DESCRIPTION = "Task를 수정합니다.";
    private static final String TASK_STATUS_UPDATE_DESCRIPTION = "Task 상태를 수정합니다.";

    private TaskUpdateSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static RestDocumentationFilter createTaskUpdateFilter(
        final String identifier,
        final String requestSchema,
        final String responseSchema
    ) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(HEADER_DESCRIPTOR)
                .requestSchema(Schema.schema(requestSchema))
                .requestFields(TASK_UPDATE_REQUEST_FIELDS)
                .responseSchema(Schema.schema(responseSchema))
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
                    requestHeaders(List.of(HEADER_DESCRIPTOR)),
                    requestFields(Arrays.stream(TASK_UPDATE_REQUEST_FIELDS).toList()),
                    responseFields(Arrays.stream(TASK_UPDATE_RESPONSE_FIELD_DESCRIPTOR).toList())
                );
            }
        );
    }

    public static RestDocumentationFilter createTaskUpdateErrorFilter(
        final String identifier,
        final String requestSchema,
        final String responseSchema
    ) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(HEADER_DESCRIPTOR)
                .requestSchema(Schema.schema(requestSchema))
                .requestFields(TASK_UPDATE_REQUEST_FIELDS)
                .responseSchema(Schema.schema(responseSchema))
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
                    requestHeaders(List.of(HEADER_DESCRIPTOR)),
                    requestFields(Arrays.stream(TASK_UPDATE_REQUEST_FIELDS).toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE).toList())
                );
            }
        );
    }

    public static RestDocumentationFilter createTaskStatusUpdateFilter(
        final String identifier,
        final String requestSchema,
        final String responseSchema
    ) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(HEADER_DESCRIPTOR)
                .requestSchema(Schema.schema(requestSchema))
                .requestFields(TASK_STATUS_UPDATE_REQUEST_FIELDS)
                .responseSchema(Schema.schema(responseSchema))
                .responseFields(TASK_STATUS_UPDATE_RESPONSE)
                .tag(TAG)
                .summary(TASK_STATUS_UPDATE_SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(TASK_UPDATE_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestHeaders(List.of(HEADER_DESCRIPTOR)),
                    requestFields(Arrays.stream(TASK_STATUS_UPDATE_REQUEST_FIELDS).toList()),
                    responseFields(Arrays.stream(TASK_STATUS_UPDATE_RESPONSE).toList())
                );
            }
        );
    }

    public static RestDocumentationFilter createTaskStatusUpdateErrorFilter(
        final String identifier,
        final String requestSchema,
        final String responseSchema
    ) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(HEADER_DESCRIPTOR)
                .requestSchema(Schema.schema(requestSchema))
                .requestFields(TASK_STATUS_UPDATE_REQUEST_FIELDS)
                .responseSchema(Schema.schema(responseSchema))
                .responseFields(TASK_UPDATE_RESPONSE_FIELD_DESCRIPTOR)
                .tag(TAG)
                .summary(TASK_STATUS_UPDATE_SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(TASK_UPDATE_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestHeaders(List.of(HEADER_DESCRIPTOR)),
                    requestFields(Arrays.stream(TASK_STATUS_UPDATE_REQUEST_FIELDS).toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE).toList())
                );
            }
        );
    }
}
