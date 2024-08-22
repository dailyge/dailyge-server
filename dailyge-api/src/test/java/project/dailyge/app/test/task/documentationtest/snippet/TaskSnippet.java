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
import org.springframework.restdocs.request.QueryParametersSnippet;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static project.dailyge.app.common.SnippetUtils.getAttribute;
import project.dailyge.app.core.task.presentation.requesst.TaskCreateRequest;
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
            .attributes(getAttribute(TaskCreateRequest.class, "date")),
    };

    FieldDescriptor[] TASK_CREATE_REQUEST_FIELDS = {
        fieldWithPath("monthlyTaskId").description("월간 일정표 ID")
            .attributes(getAttribute(TaskCreateRequest.class, "monthlyTaskId")),
        fieldWithPath("title").description("제목")
            .attributes(getAttribute(TaskCreateRequest.class, "title")),
        fieldWithPath("content").description("내용")
            .attributes(getAttribute(TaskCreateRequest.class, "content")),
        fieldWithPath("date").description("날짜")
            .attributes(getAttribute(TaskCreateRequest.class, "date")),
        fieldWithPath("color").description("색상")
            .attributes(getAttribute(TaskCreateRequest.class, "color")),
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
        fieldWithPath("color").description("색상")
            .attributes(getAttribute(TaskUpdateRequest.class, "color")),
    };

    FieldDescriptor[] TASK_STATUS_UPDATE_REQUEST_FIELDS = {
        fieldWithPath("monthlyTaskId").description("월간 일정표 ID")
            .attributes(getAttribute(TaskStatusUpdateRequest.class, "monthlyTaskId")),
        fieldWithPath("status").description("상태")
            .attributes(getAttribute(TaskStatusUpdateRequest.class, "status")),
    };

    FieldDescriptor[] MONTHLY_TASK_CREATE_RESPONSE = {
        fieldWithPath("data").type(NULL).description("데이터"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    FieldDescriptor[] TASK_CREATE_RESPONSE = {
        fieldWithPath("data.taskId").type(NUMBER).description("Task ID"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    FieldDescriptor[] TASK_UPDATE_RESPONSE_FIELD_DESCRIPTOR = {
        fieldWithPath("data").type(NULL).description("데이터"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    FieldDescriptor[] TASK_STATUS_UPDATE_RESPONSE = {
        fieldWithPath("data").type(NULL).description("데이터"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    ParameterDescriptor[] MONTHLY_TASK_PATH_PARAMETER_DESCRIPTORS = {
        parameterWithName("monthlyTaskId").description("월간 일정표 ID")
    };

    ParameterDescriptor[] DATE_QUERY_PARAMETER_DESCRIPTORS = {
        parameterWithName("date").description("날짜").attributes(
            key("constraints").value("- Must be not null.")
        )
    };

    ParameterDescriptor[] TASK_ID_PATH_PARAMETER_DESCRIPTORS = {
        parameterWithName("taskId").description("Task ID")
    };

    FieldDescriptor[] MONTHLY_TASK_ID_READ_RESPONSE_FIELD_DESCRIPTOR = {
        fieldWithPath("data.monthlyTaskId").type(NUMBER).description("월간 일정표 ID"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    FieldDescriptor[] MONTHLY_TASK_READ_RESPONSE_FIELD_DESCRIPTOR = {
        fieldWithPath("data.monthlyTaskId").type(NUMBER).description("월간 일정표 ID"),
        fieldWithPath("data.year").type(NUMBER).description("년"),
        fieldWithPath("data.month").type(NUMBER).description("월"),
        fieldWithPath("data.tasks.[].taskId").type(NUMBER).description("Task ID"),
        fieldWithPath("data.tasks.[].monthlyTaskId").type(NUMBER).description("월간 일정표 ID"),
        fieldWithPath("data.tasks.[].title").type(STRING).description("제목"),
        fieldWithPath("data.tasks.[].content").type(STRING).description("내용"),
        fieldWithPath("data.tasks.[].day").type(NUMBER).description("날짜"),
        fieldWithPath("data.tasks.[].weekOfMonth").type(NUMBER).description("주 차"),
        fieldWithPath("data.tasks.[].status").type(STRING).description("상태"),
        fieldWithPath("data.tasks.[].color").type(STRING).description("색상"),
        fieldWithPath("data.tasks.[].userId").type(NUMBER).description("사용자 ID"),
        fieldWithPath("data.tasks.[].createdAt").type(STRING).description("등록일"),
        fieldWithPath("data.tasks.[].lastModifiedAt").type(STRING).description("최종 수정일"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    FieldDescriptor[] TASK_DETAIL_SEARCH_RESPONSE_FIELD_DESCRIPTOR = {
        fieldWithPath("data.taskId").type(NUMBER).description("Task ID"),
        fieldWithPath("data.monthlyTaskId").type(STRING).description("Task ID"),
        fieldWithPath("data.title").type(STRING).description("제목"),
        fieldWithPath("data.content").type(STRING).description("내용"),
        fieldWithPath("data.day").type(NUMBER).description("날짜"),
        fieldWithPath("data.weekOfMonth").type(NUMBER).description("주 차"),
        fieldWithPath("data.status").type(STRING).description("상태"),
        fieldWithPath("data.color").type(STRING).description("색상"),
        fieldWithPath("data.userId").type(NUMBER).description("사용자 ID"),
        fieldWithPath("data.createdAt").type(STRING).description("등록일"),
        fieldWithPath("data.lastModifiedAt").type(STRING).description("최종 수정일"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    FieldDescriptor[] TASK_STATUS_LIST_READ_RESPONSE_FIELD_DESCRIPTOR = {
        fieldWithPath("data.[].kr").type(STRING).description("Task 상태(국문)"),
        fieldWithPath("data.[].en").type(STRING).description("Task 상태(영문)"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    FieldDescriptor[] ERROR_RESPONSE = {
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
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

    // Search
    PathParametersSnippet TASK_PATH_PARAMETER_SNIPPET = pathParameters(TASK_ID_PATH_PARAMETER_DESCRIPTORS);
    QueryParametersSnippet DATE_REQUEST_PARAMETER_SNIPPET = queryParameters(DATE_QUERY_PARAMETER_DESCRIPTORS);
    QueryParametersSnippet TASK_DATE_REQUEST_PARAMETER_SNIPPET = queryParameters(DATE_QUERY_PARAMETER_DESCRIPTORS);
    ResponseFieldsSnippet MONTHLY_TASK_READ_RESPONSE_SNIPPET = responseFields(MONTHLY_TASK_READ_RESPONSE_FIELD_DESCRIPTOR);
    ResponseFieldsSnippet TASK_DETAIL_SEARCH_RESPONSE_SNIPPET = responseFields(TASK_DETAIL_SEARCH_RESPONSE_FIELD_DESCRIPTOR);

    // TaskStatusRead Response Snippet
    ResponseFieldsSnippet TASK_STATUS_READ_RESPONSE_FIELD_SNIPPET = responseFields(TASK_STATUS_LIST_READ_RESPONSE_FIELD_DESCRIPTOR);

    static String createIdentifier(
        final String name,
        final int code
    ) {
        return String.format("%s/%d", name, code);
    }
}
