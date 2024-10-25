package project.dailyge.app.test.holiday.documentationtest.snippet;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import static project.dailyge.app.common.CommonSnippet.COOKIE_HEADER_DESCRIPTORS;
import static project.dailyge.app.common.CommonSnippet.TOKEN_COOKIE_DESCRIPTORS;
import static project.dailyge.app.test.task.documentationtest.snippet.TaskSnippet.ERROR_RESPONSE;

import java.util.Arrays;
import java.util.List;

public final class HolidayReadSnippet implements HolidaySnippet {

    private static final String SUMMARY = "Holiday 조회 API";
    private static final String DESCRIPTION = "Holiday를 조회합니다.";

    private HolidayReadSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static RestDocumentationFilter createHolidayReadFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(COOKIE_HEADER_DESCRIPTORS)
                .queryParameters(HOLIDAY_YEAR_PARAMETER_DESCRIPTORS)
                .responseFields(HOLIDAYS_READ_RESPONSE)
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
                    queryParameters(HOLIDAY_YEAR_PARAMETER_DESCRIPTORS),
                    responseFields(Arrays.stream(HOLIDAYS_READ_RESPONSE).toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE).toList())
                );
            }
        );
    }
}
