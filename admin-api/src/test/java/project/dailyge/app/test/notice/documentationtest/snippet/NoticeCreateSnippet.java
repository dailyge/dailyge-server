package project.dailyge.app.test.notice.documentationtest.snippet;

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

public final class NoticeCreateSnippet implements NoticeSnippet {

    private static final String NOTICE_CREATE_SUMMARY = "공지사항 생성 API";
    private static final String NOTICE_CREATE_DESCRIPTION = "공지사항을 생성하는 API 입니다.";

    public static RestDocumentationFilter createNoticeCreateFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .tag(tag)
                .requestFields(NOTICE_CREATE_REQUEST_FIELDS_DESCRIPTORS)
                .responseFields(NOTICE_CREATE_RESPONSE_FIELDS_DESCRIPTORS)
                .summary(NOTICE_CREATE_SUMMARY)
                .description(NOTICE_CREATE_DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(NOTICE_ACCESS_TOKEN_COOKIE_DESCRIPTOR),
                    requestFields(Arrays.stream(NOTICE_CREATE_REQUEST_FIELDS_DESCRIPTORS).toList()),
                    responseFields(Arrays.stream(NOTICE_CREATE_RESPONSE_FIELDS_DESCRIPTORS).toList())
                );
            }
        );
    }
}
