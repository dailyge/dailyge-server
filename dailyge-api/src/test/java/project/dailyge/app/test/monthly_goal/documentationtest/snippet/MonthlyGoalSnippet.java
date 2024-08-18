package project.dailyge.app.test.monthly_goal.documentationtest.snippet;

import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.NUMBER;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.STRING;
import org.springframework.restdocs.headers.HeaderDescriptor;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import static project.dailyge.app.common.SnippetUtils.getAttribute;
import project.dailyge.app.core.monthly_goal.presentation.request.MonthlyGoalCreateRequest;

import java.util.List;

public interface MonthlyGoalSnippet {
    String tag = "MonthlyGoal";
    String identifier = "{class_name}/{method_name}/";

    HeaderDescriptor HEADER_DESCRIPTOR = headerWithName("Authorization").description("인증 토큰").optional();
    RequestHeadersSnippet MONTHLY_GOAL_AUTHORIZATION_HEADER = requestHeaders(
        List.of(HEADER_DESCRIPTOR)
    );

    FieldDescriptor[] MONTHLY_GOAL_CREATE_REQUEST_FIELDS_DESCRIPTORS = {
        fieldWithPath("title").description("제목")
            .attributes(getAttribute(MonthlyGoalCreateRequest.class, "title")),
        fieldWithPath("content").description("내용")
            .attributes(getAttribute(MonthlyGoalCreateRequest.class, "content")),
    };

    FieldDescriptor[] MONTHLY_GOAL_CREATE_RESPONSE_FIELD_DESCRIPTORS = {
        fieldWithPath("data.monthlyGoalId").type(NUMBER).description("MonthlyGoal ID"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    FieldDescriptor[] ERROR_RESPONSE_FIELD_DESCRIPTORS = {
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    RequestFieldsSnippet MONTHLY_GOAL_CREATE_REQUEST_SNIPPET = requestFields(MONTHLY_GOAL_CREATE_REQUEST_FIELDS_DESCRIPTORS);
    ResponseFieldsSnippet MONTHLY_GOAL_CREATE_RESPONSE_SNIPPET = responseFields(MONTHLY_GOAL_CREATE_RESPONSE_FIELD_DESCRIPTORS);

    static String createIdentifier(
        final String name,
        final int code
    ) {
        return String.format("%s/%d", name, code);
    }
}
