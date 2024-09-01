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

public final class UserBlacklistSnippet implements UserSnippet {

    private static final String USER_BLACKLIST_CREATE_SUMMARY = "User 블랙리스트 생성 API";
    private static final String USER_DETAIL_SEARCH_DESCRIPTION = "User 블랙리스트를 생성하는 API 입니다.";

    public static RestDocumentationFilter createUserBlacklistCreateFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .pathParameters(USER_BLACKLIST_CREATE_PATH_DESCRIPTOR)
                .requestFields(USER_BLACKLIST_CREATE_REQUEST_FIELDS_DESCRIPTORS)
                .responseFields(USER_BLACKLIST_CREATE_RESPONSE_FIELDS_DESCRIPTORS)
                .summary(USER_BLACKLIST_CREATE_SUMMARY)
                .description(USER_DETAIL_SEARCH_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(USER_BLACKLIST_ACCESS_TOKEN_COOKIE_DESCRIPTOR),
                    pathParameters(USER_BLACKLIST_CREATE_PATH_DESCRIPTOR),
                    requestFields(Arrays.stream(USER_BLACKLIST_CREATE_REQUEST_FIELDS_DESCRIPTORS).toList()),
                    responseFields(Arrays.stream(USER_BLACKLIST_CREATE_RESPONSE_FIELDS_DESCRIPTORS).toList())
                );
            }
        );
    }
}
