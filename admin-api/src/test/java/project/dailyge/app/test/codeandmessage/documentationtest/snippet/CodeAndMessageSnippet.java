package project.dailyge.app.test.codeandmessage.documentationtest.snippet;

import static java.sql.JDBCType.ARRAY;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.NUMBER;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.STRING;
import org.springframework.restdocs.payload.FieldDescriptor;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public interface CodeAndMessageSnippet {

    String tag = "CodeAndMessage";

    FieldDescriptor[] CODE_AND_MESSAGE_RESPONSE_FILE_DESCRIPTOR = {
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지"),
        fieldWithPath("data").type(OBJECT).description("데이터"),
        fieldWithPath("data.domains").type(ARRAY).description("도메인 리스트"),
        fieldWithPath("data.domains[]").type(STRING).description("도메인 이름"),
        fieldWithPath("data.map").type(OBJECT).description("도메인별 메시지 맵"),

        fieldWithPath("data.map.common").type(ARRAY).description("Common 도메인 메시지 리스트"),
        fieldWithPath("data.map.common[].id").type(NUMBER).description("메시지 ID"),
        fieldWithPath("data.map.common[].domain").type(STRING).description("도메인 이름"),
        fieldWithPath("data.map.common[].name").type(STRING).description("이름"),
        fieldWithPath("data.map.common[].message").type(STRING).description("응답 메시지"),
        fieldWithPath("data.map.common[].code").type(NUMBER).description("응답 코드"),

        fieldWithPath("data.map.users").type(ARRAY).description("Users 도메인 메시지 리스트"),
        fieldWithPath("data.map.users[].id").type(NUMBER).description("메시지 ID"),
        fieldWithPath("data.map.users[].domain").type(STRING).description("도메인 이름"),
        fieldWithPath("data.map.users[].name").type(STRING).description("이름"),
        fieldWithPath("data.map.users[].message").type(STRING).description("응답 메시지"),
        fieldWithPath("data.map.users[].code").type(NUMBER).description("응답 코드"),

        fieldWithPath("data.map.tasks").type(ARRAY).description("Tasks 도메인 메시지 리스트"),
        fieldWithPath("data.map.tasks[].id").type(NUMBER).description("메시지 ID"),
        fieldWithPath("data.map.tasks[].domain").type(STRING).description("도메인 이름"),
        fieldWithPath("data.map.tasks[].name").type(STRING).description("이름"),
        fieldWithPath("data.map.tasks[].message").type(STRING).description("응답 메시지"),
        fieldWithPath("data.map.tasks[].code").type(NUMBER).description("응답 코드")
    };

    ResponseFieldsSnippet CODE_AND_MESSAGE_RESPONSE_FIELD_SNIPPET = responseFields(
        CODE_AND_MESSAGE_RESPONSE_FILE_DESCRIPTOR
    );

    static String createIdentifier(
        final String name,
        final int code
    ) {
        return String.format("%s/%d", name, code);
    }
}
