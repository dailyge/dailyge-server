package project.dailyge.app.test.task.documentationtest.snippet;

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

public final class TaskDeleteSnippet implements TaskSnippet {

    private static final String TASK_DELETE_SUMMARY = "Task 삭제 API";
    private static final String TASK_DELETE_DESCRIPTION = "Task를 삭제합니다.";

    private TaskDeleteSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static RestDocumentationFilter createTaskDeleteFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .pathParameters(TASK_ID_PATH_PARAMETER_DESCRIPTORS)
                .tag(TAG)
                .summary(TASK_DELETE_SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(TASK_DELETE_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(TASK_TOKEN_COOKIE_DESCRIPTORS),
                    pathParameters(Arrays.stream(TASK_ID_PATH_PARAMETER_DESCRIPTORS).toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE).toList())
                );
            }
        );
    }
}
