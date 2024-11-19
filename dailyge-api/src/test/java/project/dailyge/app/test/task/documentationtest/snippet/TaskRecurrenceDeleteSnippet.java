package project.dailyge.app.test.task.documentationtest.snippet;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.springframework.restdocs.restassured.RestDocumentationFilter;

import java.util.Arrays;
import java.util.List;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static project.dailyge.app.common.CommonSnippet.COOKIE_HEADER_DESCRIPTORS;

public class TaskRecurrenceDeleteSnippet implements TaskSnippet {

    private static final String SUMMARY = "반복 일정 삭제 API";
    private static final String DESCRIPTION = "반복 일정 정보를 삭제합니다.";

    private TaskRecurrenceDeleteSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static RestDocumentationFilter createTaskRecurrenceFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(COOKIE_HEADER_DESCRIPTORS)
                .pathParameters(TASK_RECURRENCE_ID_PATH_PARAMETER_DESCRIPTORS)
                .tag(TAG)
                .summary(SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(TASK_TOKEN_COOKIE_DESCRIPTORS),
                    requestFields(Arrays.stream(TASK_RECURRENCE_UPDATE_REQUEST_FIELDS).toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE).toList())
                );
            }
        );
    }
}
