package project.dailyge.app.test.task.documentationtest.snippet;

import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.NUMBER;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.STRING;
import org.springframework.restdocs.headers.HeaderDescriptor;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import static org.springframework.restdocs.payload.JsonFieldType.NULL;
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
import project.dailyge.app.core.task.presentation.requesst.TaskRegisterRequest;
import project.dailyge.app.core.task.presentation.requesst.TaskStatusUpdateRequest;
import project.dailyge.app.core.task.presentation.requesst.TaskUpdateRequest;

import java.util.List;

public interface TaskSnippet {

    String TAG = "Task";
    String identifier = "{class_name}/{method_name}/";

    HeaderDescriptor HEADER_DESCRIPTOR = headerWithName("Authorization").description("인증 토큰").optional();

    RequestHeadersSnippet TASK_AUTHORIZATION_HEADER = requestHeaders(
        List.of(HEADER_DESCRIPTOR)
    );

    FieldDescriptor[] MONTHLY_TASK_CREATE_REQUEST_FIELDS = {
        fieldWithPath("date").description("날짜")
            .attributes(getAttribute(TaskRegisterRequest.class, "date")),
    };

    FieldDescriptor[] TASK_CREATE_REQUEST_FIELDS = {
        fieldWithPath("monthlyTaskId").description("월간 일정표 ID")
            .attributes(getAttribute(TaskRegisterRequest.class, "monthlyTaskId")),
        fieldWithPath("title").description("제목")
            .attributes(getAttribute(TaskRegisterRequest.class, "title")),
        fieldWithPath("content").description("내용")
            .attributes(getAttribute(TaskRegisterRequest.class, "content")),
        fieldWithPath("date").description("날짜")
            .attributes(getAttribute(TaskRegisterRequest.class, "date")),
    };

    FieldDescriptor[] TASK_UPDATE_REQUEST_FIELDS = {
        fieldWithPath("monthlyTaskId").description("월간 일정표 ID")
            .attributes(getAttribute(TaskUpdateRequest.class, "monthlyTaskId")),
        fieldWithPath("title").description("제목")
            .attributes(getAttribute(TaskUpdateRequest.class, "title")),
        fieldWithPath("content").description("내용")
            .attributes(getAttribute(TaskUpdateRequest.class, "content")),
        fieldWithPath("date").description("날짜")
            .attributes(getAttribute(TaskUpdateRequest.class, "date")),
        fieldWithPath("status").description("상태")
            .attributes(getAttribute(TaskUpdateRequest.class, "status")),
    };

    FieldDescriptor[] TASK_STATUS_UPDATE_REQUEST_FIELDS = {
        fieldWithPath("monthlyTaskId").description("월간 일정표 ID")
            .attributes(getAttribute(TaskStatusUpdateRequest.class, "monthlyTaskId")),
        fieldWithPath("status").description("상태")
            .attributes(getAttribute(TaskStatusUpdateRequest.class, "status")),
    };

    FieldDescriptor[] MONTHLY_TASK_CREATE_RESPONSE = {
        fieldWithPath("data").type(NULL).description("데이터"),
        fieldWithPath("code").type(STRING).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    FieldDescriptor[] TASK_CREATE_RESPONSE = {
        fieldWithPath("data.taskId").type(STRING).description("할 일 ID"),
        fieldWithPath("code").type(STRING).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    FieldDescriptor[] TASK_UPDATE_RESPONSE_FIELD_DESCRIPTOR = {
        fieldWithPath("data").type(NULL).description("데이터"),
        fieldWithPath("code").type(STRING).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    FieldDescriptor[] TASK_STATUS_UPDATE_RESPONSE = {
        fieldWithPath("data").type(NULL).description("데이터"),
        fieldWithPath("code").type(STRING).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    ParameterDescriptor[] TASK_PATH_PARAMETER_DESCRIPTORS = {
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

    FieldDescriptor[] ERROR_RESPONSE = {
        fieldWithPath("code").type(STRING).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    RequestFieldsSnippet MONTHLY_TASK_CREATE_REQUEST_SNIPPET = requestFields(MONTHLY_TASK_CREATE_REQUEST_FIELDS);
    RequestFieldsSnippet TASK_CREATE_REQUEST_SNIPPET = requestFields(TASK_CREATE_REQUEST_FIELDS);
    ResponseFieldsSnippet MONTHLY_TASK_CREATE_RESPONSE_SNIPPET = responseFields(MONTHLY_TASK_CREATE_RESPONSE);
    ResponseFieldsSnippet TASK_CREATE_RESPONSE_SNIPPET = responseFields(TASK_CREATE_RESPONSE);

    RequestFieldsSnippet TASK_UPDATE_REQUEST_SNIPPET = requestFields(TASK_UPDATE_REQUEST_FIELDS);
    RequestFieldsSnippet TASK_STATUS_UPDATE_REQUEST_SNIPPET = requestFields(TASK_STATUS_UPDATE_REQUEST_FIELDS);
    ResponseFieldsSnippet TASK_UPDATE_RESPONSE_SNIPPET = responseFields(TASK_UPDATE_RESPONSE_FIELD_DESCRIPTOR);
    ResponseFieldsSnippet TASK_STATUS_UPDATE_RESPONSE_SNIPPET = responseFields(TASK_STATUS_UPDATE_RESPONSE);

    // Detail Search
    PathParametersSnippet TASK_PATH_PARAMETER_SNIPPET = pathParameters(TASK_PATH_PARAMETER_DESCRIPTORS);
    ResponseFieldsSnippet TASK_DETAIL_SEARCH_RESPONSE_SNIPPET = responseFields(TASK_DETAIL_SEARCH_RESPONSE_FIELD_DESCRIPTOR);

    // Delete
    PathParametersSnippet TASK_DELETE_PATH_PARAMETER_SNIPPET = pathParameters(TASK_DELETE_PATH_DESCRIPTOR);

    static String createIdentifier(
        final String name,
        final int code
    ) {
        return String.format("%s/%d", name, code);
    }
}
