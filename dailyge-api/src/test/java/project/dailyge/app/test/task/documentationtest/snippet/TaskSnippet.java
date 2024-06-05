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
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.PathParametersSnippet;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
        fieldWithPath("data.taskId").type(NUMBER).description("할 일 ID"),
        fieldWithPath("code").type(STRING).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    ParameterDescriptor[] TASK_DETAIL_SEARCH_PATH_DESCRIPTOR = {
        parameterWithName("taskId").description("할 일 ID")
    };

    ParameterDescriptor[] TASK_DELETE_PATH_DESCRIPTOR = {
        parameterWithName("taskId").description("할 일 ID")
    };

    FieldDescriptor[] TASK_DETAIL_SEARCH_RESPONSE_FIELD_DESCRIPTOR = {
        fieldWithPath("data.id").type(NUMBER).description("할 일 ID"),
        fieldWithPath("data.title").type(STRING).description("제목"),
        fieldWithPath("data.content").type(STRING).description("내용"),
        fieldWithPath("data.date").type(STRING).description("날짜"),
        fieldWithPath("data.status").type(STRING).description("상태"),
        fieldWithPath("code").type(STRING).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    RequestFieldsSnippet TASK_CREATE_REQUEST_SNIPPET = requestFields(TASK_CREATE_REQUEST_FIELDS);
    ResponseFieldsSnippet TASK_CREATE_RESPONSE_SNIPPET = responseFields(TASK_CREATE_RESPONSE);

    // Detail Search
    PathParametersSnippet TASK_DETAIL_SEARCH_PATH_PARAMETER_SNIPPET = pathParameters(TASK_DETAIL_SEARCH_PATH_DESCRIPTOR);
    ResponseFieldsSnippet TASK_DETAIL_SEARCH_RESPONSE_SNIPPET = responseFields(TASK_DETAIL_SEARCH_RESPONSE_FIELD_DESCRIPTOR);

    // Delete
    PathParametersSnippet TASK_DELETE_PATH_PARAMETER_SNIPPET = pathParameters(TASK_DELETE_PATH_DESCRIPTOR);
}
