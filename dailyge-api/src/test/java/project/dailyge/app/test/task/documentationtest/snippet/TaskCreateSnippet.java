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

public final class TaskCreateSnippet implements TaskSnippet {

    private static final String TASK_CREATE_SUMMARY = "Task 생성 API";
    private static final String MONTHLY_TASK_CREATE_SUMMARY = "Monthly Task 생성 API";
    private static final String TASK_LABEL_CREATE_SUMMARY = "Task Label 생성 API";

    private static final String TASK_CREATE_DESCRIPTION = "Task를 생성하는 API 입니다.";
    private static final String MONTHLY_TASK_CREATE_DESCRIPTION = "Monthly Task를 생성하는 API 입니다.";
    private static final String TASK_LABEL_CREATE_DESCRIPTION = "Task Label을 생성하는 API 입니다.";


    private TaskCreateSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static RestDocumentationFilter createTasksFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(COOKIE_HEADER_DESCRIPTORS)
                .requestFields(TASK_CREATE_REQUEST_FIELDS)
                .responseFields(TASK_CREATE_RESPONSE)
                .tag(TAG)
                .summary(TASK_CREATE_SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(TASK_CREATE_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(TASK_TOKEN_COOKIE_DESCRIPTORS),
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
                .requestFields(MONTHLY_TASK_CREATE_REQUEST_FIELDS)
                .responseFields(MONTHLY_TASK_CREATE_RESPONSE)
                .tag(TAG)
                .summary(MONTHLY_TASK_CREATE_SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(MONTHLY_TASK_CREATE_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(TASK_TOKEN_COOKIE_DESCRIPTORS),
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
                .requestFields(MONTHLY_TASK_CREATE_REQUEST_FIELDS)
                .responseFields(ERROR_RESPONSE)
                .tag(TAG)
                .summary(MONTHLY_TASK_CREATE_SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(MONTHLY_TASK_CREATE_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(TASK_TOKEN_COOKIE_DESCRIPTORS),
                    requestFields(Arrays.stream(MONTHLY_TASK_CREATE_REQUEST_FIELDS).toList()),
                    responseFields(Arrays.stream(MONTHLY_TASK_CREATE_RESPONSE).toList())
                );
            }
        );
    }

    public static RestDocumentationFilter createTaskLabelsFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestFields(TASK_LABEL_CREATE_REQUEST_FIELDS_DESCRIPTOR)
                .responseFields(TASK_LABEL_CREATE_RESPONSE_FIELD_DESCRIPTOR)
                .tag(TAG)
                .summary(TASK_LABEL_CREATE_SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(TASK_LABEL_CREATE_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(TASK_TOKEN_COOKIE_DESCRIPTORS),
                    requestFields(Arrays.stream(TASK_LABEL_CREATE_REQUEST_FIELDS_DESCRIPTOR).toList()),
                    responseFields(Arrays.stream(TASK_LABEL_CREATE_RESPONSE_FIELD_DESCRIPTOR).toList())
                );
            }
        );
    }
}
