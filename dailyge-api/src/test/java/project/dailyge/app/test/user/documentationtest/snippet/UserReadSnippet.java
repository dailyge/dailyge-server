package project.dailyge.app.test.user.documentationtest.snippet;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import java.util.Arrays;
import java.util.List;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

public final class UserReadSnippet implements UserSnippet {

    private static final String USER_DETAIL_SEARCH_SUMMARY = "User 조회 API";
    private static final String USER_DETAIL_SEARCH_DESCRIPTION = "User 정보를 조회하는 API 입니다.";

    private UserReadSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    /**
     * User 조회 문서
     */
    public static RestDocumentationFilter createUserSearchFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .responseFields(USER_SEARCH_RESPONSE_FIELD_DESCRIPTOR)
                .tag(TAG)
                .summary(USER_DETAIL_SEARCH_SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(USER_DETAIL_SEARCH_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(USER_ACCESS_TOKEN_COOKIE_DESCRIPTOR),
                    responseFields(Arrays.stream(USER_SEARCH_RESPONSE_FIELD_DESCRIPTOR).toList()),
                    responseFields(Arrays.stream(RESPONSE_STATUS).toList())
                );
            }
        );
    }
}
