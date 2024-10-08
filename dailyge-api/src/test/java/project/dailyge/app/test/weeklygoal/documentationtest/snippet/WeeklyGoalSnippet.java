package project.dailyge.app.test.weeklygoal.documentationtest.snippet;

import org.springframework.restdocs.cookies.CookieDescriptor;
import org.springframework.restdocs.cookies.RequestCookiesSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import project.dailyge.app.core.weeklygoal.request.WeeklyGoalCreateRequest;

import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.NUMBER;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.STRING;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static project.dailyge.app.common.SnippetUtils.getAttribute;

public interface WeeklyGoalSnippet {
    String tag = "WeeklyGoal";
    String identifier = "{class_name}/{method_name}/";

    CookieDescriptor[] WEEKLY_GOAL_TOKEN_COOKIE_DESCRIPTORS = {
        cookieWithName("Access-Token").description("인증 토큰")
    };

    RequestCookiesSnippet WEEKLY_GOAL_ACCESS_TOKEN_COOKIE_SNIPPET = requestCookies(WEEKLY_GOAL_TOKEN_COOKIE_DESCRIPTORS);

    FieldDescriptor[] WEEKLY_GOAL_CREATE_REQUEST_FIELDS_DESCRIPTORS = {
        fieldWithPath("title").description("제목")
            .attributes(getAttribute(WeeklyGoalCreateRequest.class, "title")),
        fieldWithPath("content").description("내용")
            .attributes(getAttribute(WeeklyGoalCreateRequest.class, "content")),
        fieldWithPath("date").description("날짜")
            .attributes(getAttribute(WeeklyGoalCreateRequest.class, "date"))
    };

    FieldDescriptor[] WEEKLY_GOAL_CREATE_RESPONSE_FIELD_DESCRIPTORS = {
        fieldWithPath("data.weeklyGoalId").type(NUMBER).description("Weekly Goal ID"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };


    FieldDescriptor[] ERROR_RESPONSE_FIELD_DESCRIPTORS = {
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    RequestFieldsSnippet WEEKLY_GOAL_CREATE_REQUEST_SNIPPET = requestFields(WEEKLY_GOAL_CREATE_REQUEST_FIELDS_DESCRIPTORS);
    ResponseFieldsSnippet WEEKLY_GOAL_CREATE_RESPONSE_SNIPPET = responseFields(WEEKLY_GOAL_CREATE_RESPONSE_FIELD_DESCRIPTORS);

    static String createIdentifier(
        final String name,
        final int code
    ) {
        return String.format("%s/%d", name, code);
    }
}
