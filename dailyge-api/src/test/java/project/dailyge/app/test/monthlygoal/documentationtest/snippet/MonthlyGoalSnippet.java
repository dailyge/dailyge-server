package project.dailyge.app.test.monthlygoal.documentationtest.snippet;

import org.springframework.restdocs.cookies.CookieDescriptor;
import org.springframework.restdocs.cookies.RequestCookiesSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.PathParametersSnippet;
import project.dailyge.app.core.monthlygoal.presentation.request.MonthlyGoalCreateRequest;
import project.dailyge.app.core.monthlygoal.presentation.request.MonthlyGoalStatusUpdateRequest;
import project.dailyge.app.core.monthlygoal.presentation.request.MonthlyGoalUpdateRequest;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.NUMBER;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.STRING;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.payload.JsonFieldType.NULL;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static project.dailyge.app.common.SnippetUtils.getAttribute;

public interface MonthlyGoalSnippet {
    String tag = "MonthlyGoal";
    String identifier = "{class_name}/{method_name}/";

    CookieDescriptor[] MONTHLY_GOAL_TOKEN_COOKIE_DESCRIPTORS = {
        cookieWithName("Access-Token").description("인증 토큰")
    };

    RequestCookiesSnippet MONTHLY_GOAL_ACCESS_TOKEN_COOKIE_SNIPPET = requestCookies(MONTHLY_GOAL_TOKEN_COOKIE_DESCRIPTORS);

    FieldDescriptor[] MONTHLY_GOAL_CREATE_REQUEST_FIELDS_DESCRIPTORS = {
        fieldWithPath("title").description("제목")
            .attributes(getAttribute(MonthlyGoalCreateRequest.class, "title")),
        fieldWithPath("content").description("내용")
            .attributes(getAttribute(MonthlyGoalCreateRequest.class, "content")),
    };

    FieldDescriptor[] MONTHLY_GOAL_STATUS_UPDATE_REQUEST_FIELDS_DESCRIPTORS = {
        fieldWithPath("done").description("목표 달성 상태")
            .attributes(getAttribute(MonthlyGoalStatusUpdateRequest.class, "done"))
    };

    FieldDescriptor[] MONTHLY_GOAL_UPDATE_REQUEST_FIELDS_DESCRIPTORS = {
        fieldWithPath("title").description("제목")
            .attributes(getAttribute(MonthlyGoalUpdateRequest.class, "title")),
        fieldWithPath("content").description("내용")
            .attributes(getAttribute(MonthlyGoalUpdateRequest.class, "content")),
    };

    FieldDescriptor[] MONTHLY_GOAL_CREATE_RESPONSE_FIELD_DESCRIPTORS = {
        fieldWithPath("data.monthlyGoalId").type(NUMBER).description("MonthlyGoal ID"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    FieldDescriptor[] MONTHLY_GOAL_UPDATE_RESPONSE_FIELD_DESCRIPTORS = {
        fieldWithPath("data").type(NULL).description("데이터"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    FieldDescriptor[] ERROR_RESPONSE_FIELD_DESCRIPTORS = {
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    ParameterDescriptor[] MONTHLY_GOAL_ID_PATH_PARAMETER_DESCRIPTORS = {
        parameterWithName("monthlyGoalId").description("MonthlyGoal ID")
    };

    PathParametersSnippet MONTHLY_GOAL_PATH_PARAMETER_SNIPPET = pathParameters(MONTHLY_GOAL_ID_PATH_PARAMETER_DESCRIPTORS);
    RequestFieldsSnippet MONTHLY_GOAL_CREATE_REQUEST_SNIPPET = requestFields(MONTHLY_GOAL_CREATE_REQUEST_FIELDS_DESCRIPTORS);
    RequestFieldsSnippet MONTHLY_GOAL_UPDATE_REQUEST_SNIPPET = requestFields(MONTHLY_GOAL_UPDATE_REQUEST_FIELDS_DESCRIPTORS);
    ResponseFieldsSnippet MONTHLY_GOAL_CREATE_RESPONSE_SNIPPET = responseFields(MONTHLY_GOAL_CREATE_RESPONSE_FIELD_DESCRIPTORS);
    ResponseFieldsSnippet MONTHLY_GOAL_UPDATE_RESPONSE_SNIPPET = responseFields(MONTHLY_GOAL_UPDATE_RESPONSE_FIELD_DESCRIPTORS);

    static String createIdentifier(
        final String name,
        final int code
    ) {
        return String.format("%s/%d", name, code);
    }
}
