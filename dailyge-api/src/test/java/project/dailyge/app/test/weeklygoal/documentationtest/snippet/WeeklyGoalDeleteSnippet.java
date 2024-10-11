package project.dailyge.app.test.weeklygoal.documentationtest.snippet;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import org.springframework.restdocs.restassured.RestDocumentationFilter;

import java.util.Arrays;
import java.util.List;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static project.dailyge.app.common.CommonSnippet.COOKIE_HEADER_DESCRIPTORS;

public final class WeeklyGoalDeleteSnippet implements WeeklyGoalSnippet {

    private static final String SUMMARY = "주간 목표 삭제 API";
    private static final String DESCRIPTION = "주간 목표를 삭제합니다.";

    private WeeklyGoalDeleteSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static RestDocumentationFilter deleteWeeklyGoalFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(COOKIE_HEADER_DESCRIPTORS)
                .pathParameters(WEEKLY_GOAL_ID_PATH_PARAMETER_DESCRIPTORS)
                .tag(tag)
                .summary(SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(WEEKLY_GOAL_TOKEN_COOKIE_DESCRIPTORS),
                    pathParameters(Arrays.stream(WEEKLY_GOAL_ID_PATH_PARAMETER_DESCRIPTORS).toList())
                );
            }
        );
    }

    public static RestDocumentationFilter deleteWeeklyGoalErrorFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .pathParameters(WEEKLY_GOAL_ID_PATH_PARAMETER_DESCRIPTORS)
                .responseFields(ERROR_RESPONSE_FIELD_DESCRIPTORS)
                .tag(tag)
                .summary(SUMMARY)
                .privateResource(false)
                .deprecated(false)
                .description(DESCRIPTION),
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets -> {
                List.of(
                    requestCookies(WEEKLY_GOAL_TOKEN_COOKIE_DESCRIPTORS),
                    pathParameters(Arrays.stream(WEEKLY_GOAL_ID_PATH_PARAMETER_DESCRIPTORS).toList()),
                    responseFields(ERROR_RESPONSE_FIELD_DESCRIPTORS)
                );
            }
        );
    }
}
