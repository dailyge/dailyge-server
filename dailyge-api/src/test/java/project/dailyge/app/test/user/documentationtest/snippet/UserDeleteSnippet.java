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
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public final class UserDeleteSnippet implements UserSnippet {

    private static final String USER_DELETE_SUMMARY = "User 삭제 API";
    private static final String USER_DELETE_DESCRIPTION = "User 정보를 삭제하는 API 입니다.";

    private UserDeleteSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    /**
     * User 삭제 문서
     */
    public static RestDocumentationFilter createUserDeleteFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .pathParameters(USER_DELETE_PATH_DESCRIPTOR)
                .tag(TAG)
                .summary(USER_DELETE_SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(USER_DELETE_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(USER_ACCESS_TOKEN_COOKIE_DESCRIPTOR),
                    pathParameters(USER_DELETE_PATH_DESCRIPTOR),
                    responseFields(Arrays.stream(RESPONSE_STATUS).toList()),
                    responseCookies(LOGOUT_RESPONSE_COOKIE_DESCRIPTOR)
                );
            }
        );
    }
}
