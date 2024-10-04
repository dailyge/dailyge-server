package project.dailyge.app.test.anniversary.documentationtest.snippet;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.ERROR_RESPONSE;

import java.util.Arrays;
import java.util.List;

public final class AnniversaryUpdateSnippet implements AnniversarySnippet {

    private static final String SUMMARY = "Anniversary 삭제 API";
    private static final String DESCRIPTION = "Anniversary를 삭제합니다.";

    private AnniversaryUpdateSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static RestDocumentationFilter createUpdateAnniversaryFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(COOKIE_HEADER_DESCRIPTORS)
                .pathParameters(ANNIVERSARY_ID_PATH_PARAMETER_DESCRIPTORS)
                .requestFields(ANNIVERSARY_UPDATE_REQUEST_FIELDS)
                .responseFields(ANNIVERSARY_RESPONSE)
                .tag(TAG)
                .summary(SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(TOKEN_COOKIE_DESCRIPTORS),
                    pathParameters(Arrays.stream(ANNIVERSARY_ID_PATH_PARAMETER_DESCRIPTORS).toList()),
                    requestFields(ANNIVERSARY_UPDATE_REQUEST_FIELDS),
                    responseFields(Arrays.stream(ANNIVERSARY_RESPONSE).toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE).toList())
                );
            }
        );
    }
}
