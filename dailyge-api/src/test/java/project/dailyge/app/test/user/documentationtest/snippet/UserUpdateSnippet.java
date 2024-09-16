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
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public final class UserUpdateSnippet implements UserSnippet {

    private static final String USER_UPDATE_SUMMARY = "User 수정 API";
    private static final String USER_UPDATE_DESCRIPTION = "User 정보를 수정하는 API 입니다.";

    private UserUpdateSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    /**
     * User 수정 문서
     */
    public static RestDocumentationFilter createUserUpdateFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .pathParameters(USER_UPDATE_PATH_DESCRIPTOR)
                .tag(TAG)
                .summary(USER_UPDATE_SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(USER_UPDATE_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(USER_ACCESS_TOKEN_COOKIE_DESCRIPTOR),
                    requestFields(USER_UPDATE_REQUEST_FIELD_DESCRIPTOR),
                    pathParameters(USER_UPDATE_PATH_DESCRIPTOR),
                    responseFields(Arrays.stream(LOGIN_PAGE_FIELD_DESCRIPTOR).toList())
                );
            }
        );
    }
}
