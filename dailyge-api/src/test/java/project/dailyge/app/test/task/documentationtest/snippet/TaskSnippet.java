package project.dailyge.app.test.task.documentationtest.snippet;

import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.NUMBER;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.STRING;
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

public interface TaskSnippet {

    RequestHeadersSnippet TASK_AUTHORIZATION_HEADER = requestHeaders(
        headerWithName("Authorization").description("인증 토큰").optional()
    );

    FieldDescriptor[] TASK_CREATE_REQUEST_FIELDS = {
        fieldWithPath("title").description("제목")
            .attributes(getAttribute("title")),
        fieldWithPath("content").description("내용")
            .attributes(getAttribute("content")),
        fieldWithPath("date").description("날짜")
            .attributes(getAttribute("date")),
    };

    FieldDescriptor[] TASK_CREATE_RESPONSE = {
        fieldWithPath("data.taskId").type(NUMBER).description("할 일 PK"),
        fieldWithPath("code").type(STRING).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };


    RequestFieldsSnippet TASK_CREATE_REQUEST_SNIPPET = requestFields(TASK_CREATE_REQUEST_FIELDS);
    ResponseFieldsSnippet TASK_CREATE_RESPONSE_SNIPPET = responseFields(TASK_CREATE_RESPONSE);
}
