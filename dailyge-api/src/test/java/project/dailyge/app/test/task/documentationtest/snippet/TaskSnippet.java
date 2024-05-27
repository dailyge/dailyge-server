package project.dailyge.app.test.task.documentationtest.snippet;

import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.NUMBER;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.STRING;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.snippet.Attributes;
import project.dailyge.app.core.task.dto.requesst.TaskRegisterRequest;

import java.util.List;

public interface TaskSnippet {
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

    static Attributes.Attribute getAttribute(String field) {
        StringBuilder sb = new StringBuilder();
        ConstraintDescriptions constraints = new ConstraintDescriptions(TaskRegisterRequest.class);

        List<String> fieldConstraints = constraints.descriptionsForProperty(field);
        for (String constraint : fieldConstraints) {
            sb.append("-")
                .append(" ")
                .append(constraint)
                .append(".")
                .append("\n")
                .append("\n");
        }
        return Attributes.key("constraints")
            .value(sb.toString());
    }
}
