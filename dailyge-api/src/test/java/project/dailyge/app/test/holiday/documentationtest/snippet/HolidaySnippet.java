package project.dailyge.app.test.holiday.documentationtest.snippet;

import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.NUMBER;
import static javax.xml.xpath.XPathEvaluationResult.XPathResultType.STRING;
import static org.hibernate.boot.model.source.internal.hbm.XmlElementMetadata.ARRAY;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import org.springframework.restdocs.cookies.RequestCookiesSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.QueryParametersSnippet;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static project.dailyge.app.common.CommonSnippet.TOKEN_COOKIE_DESCRIPTORS;

public interface HolidaySnippet {

    String TAG = "Holiday";
    String identifier = "{class_name}/{method_name}/";

    FieldDescriptor[] HOLIDAYS_READ_RESPONSE = {
        fieldWithPath("data.[].holidayId").type(NUMBER).description("공휴일 PK"),
        fieldWithPath("data.[].name").type(STRING).description("기념일"),
        fieldWithPath("data.[].date").type(STRING).description("날짜"),
        fieldWithPath("data").type(ARRAY).description("데이터"),
        fieldWithPath("code").type(NUMBER).description("응답 코드"),
        fieldWithPath("message").type(STRING).description("응답 메시지")
    };

    ParameterDescriptor[] HOLIDAY_YEAR_PARAMETER_DESCRIPTORS = {
        parameterWithName("year").optional().description("년도")
    };

    RequestCookiesSnippet ACCESS_TOKEN_COOKIE_SNIPPET = requestCookies(TOKEN_COOKIE_DESCRIPTORS);
    ResponseFieldsSnippet HOLIDAYS_READ_RESPONSE_SNIPPET = responseFields(HOLIDAYS_READ_RESPONSE);
    QueryParametersSnippet HOLIDAY_YEAR_QUERY_PARAMS_SNIPPET = queryParameters(HOLIDAY_YEAR_PARAMETER_DESCRIPTORS);

    static String createIdentifier(
        final String name,
        final int code
    ) {
        return String.format("%s/%d", name, code);
    }
}
