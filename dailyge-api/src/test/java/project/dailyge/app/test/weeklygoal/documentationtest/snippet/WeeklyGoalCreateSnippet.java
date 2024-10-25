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
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static project.dailyge.app.common.CommonSnippet.COOKIE_HEADER_DESCRIPTORS;

public final class WeeklyGoalCreateSnippet implements WeeklyGoalSnippet {

    private static final String SUMMARY = "주간 목표 생성 API";
    private static final String DESCRIPTION = "주간 목표를 생성합니다.";

    private WeeklyGoalCreateSnippet() {
        throw new AssertionError("올바른 방식으로 생성자를 호출해주세요.");
    }

    public static RestDocumentationFilter createWeeklyGoalFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestHeaders(COOKIE_HEADER_DESCRIPTORS)
                .requestFields(WEEKLY_GOAL_CREATE_REQUEST_FIELDS_DESCRIPTORS)
                .responseFields(WEEKLY_GOAL_CREATE_RESPONSE_FIELD_DESCRIPTORS)
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
                    requestFields(Arrays.stream(WEEKLY_GOAL_CREATE_REQUEST_FIELDS_DESCRIPTORS).toList()),
                    responseFields(Arrays.stream(WEEKLY_GOAL_CREATE_RESPONSE_FIELD_DESCRIPTORS).toList())
                );
            }
        );
    }

    public static RestDocumentationFilter createWeeklyGoalErrorFilter(final String identifier) {
        return document(
            identifier,
            ResourceSnippetParameters.builder()
                .requestFields(WEEKLY_GOAL_CREATE_REQUEST_FIELDS_DESCRIPTORS)
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
                    requestFields(Arrays.stream(WEEKLY_GOAL_CREATE_REQUEST_FIELDS_DESCRIPTORS).toList()),
                    responseFields(Arrays.stream(ERROR_RESPONSE_FIELD_DESCRIPTORS).toList())
                );
            }
        );
    }
}
