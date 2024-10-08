package project.dailyge.app.test.user.documentationtest.snippet;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import java.util.Arrays;
import java.util.List;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.cookies.CookieDocumentation.responseCookies;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

public final class LogoutSnippet implements UserSnippet {

    private static final String LOGOUT_SUMMARY = "로그아웃 API";
    private static final String LOGOUT_DESCRIPTION = "로그아웃하는 API 입니다.";

    private LogoutSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    /**
     * Logout 문서
     */
    public static RestDocumentationFilter createLogoutFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .tag(TAG)
                .summary(LOGOUT_SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(LOGOUT_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(USER_ACCESS_TOKEN_COOKIE_DESCRIPTOR),
                    responseFields(Arrays.stream(RESPONSE_STATUS).toList()),
                    responseCookies(LOGOUT_RESPONSE_COOKIE_DESCRIPTOR)
                );
            }
        );
    }
}
