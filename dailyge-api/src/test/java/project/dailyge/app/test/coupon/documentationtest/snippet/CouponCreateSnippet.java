package project.dailyge.app.test.coupon.documentationtest.snippet;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.springframework.restdocs.restassured.RestDocumentationFilter;

import java.util.Arrays;
import java.util.List;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

public final class CouponCreateSnippet implements CouponSnippet {
    private static final String SUMMARY = "Coupon 발급 신청 API";
    private static final String DESCRIPTION = "Task를 생성합니다.";

    private CouponCreateSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static RestDocumentationFilter createTasksFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(HEADER_DESCRIPTOR)
                .responseFields(COUPON_CREATE_RESPONSE_FIELDS)
                .tag(TAG)
                .summary(SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestHeaders(List.of(HEADER_DESCRIPTOR)),
                    responseFields(Arrays.stream(COUPON_CREATE_RESPONSE_FIELDS).toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE).toList())
                );
            }
        );
    }
}
