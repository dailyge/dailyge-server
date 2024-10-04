package project.dailyge.app.test.anniversary.documentationtest.snippet;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.ERROR_RESPONSE;

import java.util.Arrays;
import java.util.List;

public final class AnniversaryCreateSnippet implements AnniversarySnippet {

    private static final String SUMMARY = "Anniversary 등록 API";
    private static final String DESCRIPTION = "Anniversary를 등록합니다.";

    private AnniversaryCreateSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static RestDocumentationFilter createAnniversaryFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestFields(ANNIVERSARY_CREATE_REQUEST_FIELDS)
                .responseFields(ANNIVERSARY_CREATE_RESPONSE)
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
                    requestFields(Arrays.stream(ANNIVERSARY_CREATE_REQUEST_FIELDS).toList()),
                    responseFields(Arrays.stream(ANNIVERSARY_CREATE_RESPONSE).toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE).toList())
                );
            }
        );
    }
}
